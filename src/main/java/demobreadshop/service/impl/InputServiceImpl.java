package demobreadshop.service.impl;

import demobreadshop.constants.ConstProperties;
import demobreadshop.domain.Input;
import demobreadshop.domain.ProductList;
import demobreadshop.domain.User;
import demobreadshop.domain.WareHouse;
import demobreadshop.domain.enums.ProductType;
import demobreadshop.payload.InputDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.repository.InputRepository;
import demobreadshop.repository.UserRepository;
import demobreadshop.repository.WareHouseRepository;
import demobreadshop.service.InputService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class InputServiceImpl implements InputService {
    private final InputRepository repository;
    private final WareHouseRepository wareHouseRepository;
    private final UserRepository userRepository;

    @Autowired
    public InputServiceImpl(InputRepository repository, WareHouseRepository wareHouseRepository, UserRepository userRepository) {
        this.repository = repository;
        this.wareHouseRepository = wareHouseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Input> getAll() {
        return repository.findAllByType(ProductType.PRODUCT);
    }

    @Override
    public Input get(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Input> getAllWarehouseInputs() {
        return repository.findAllByType(ProductType.MATERIAL);
    }

    @Override
    public MyResponse create(InputDto dto) {
        final Optional<WareHouse> byId = wareHouseRepository.findById(dto.getProductId());
        if (byId.isPresent()) {
            final WareHouse product = byId.get();
            product.setAmount(product.getAmount() + dto.getAmount());
            changeMaterials(product.getMaterials(), dto.getAmount(), ConstProperties.OPERATOR_MINUS);

            Input input = repository.save(
                    new Input(
                            wareHouseRepository.save(product),
                            dto.getAmount(),
                            product.getType()
                    )
            );
            changeMoneyWithKPI(input, ConstProperties.OPERATOR_PLUS);
            return MyResponse.SUCCESSFULLY_CREATED;
        }
        return MyResponse.PRODUCT_NOT_FOUND;
    }


    @Transactional
    @Override
    public MyResponse delete(long id) {
        final Optional<Input> byId = repository.findById(id);
        if (byId.isPresent()) {
            try {
                final Input input = byId.get();
                if (AuthServiceImpl.isNonDeletable(input.getCreatedAt().getTime())) {
                    return MyResponse.CANT_DELETE;
                }

                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (!input.getCreatedBy().equals(user.getFullName())) {
                    return MyResponse.CAN_DELETE_OWN;
                }

                repository.deleteById(id);

                // back up changes
                final WareHouse product = input.getMaterial();
                product.setAmount(product.getAmount() - input.getAmount());
                changeMaterials(product.getMaterials(), input.getAmount(), ConstProperties.OPERATOR_PLUS);

                wareHouseRepository.save(product);
                changeMoneyWithKPI(input, ConstProperties.OPERATOR_MINUS);
                return MyResponse.SUCCESSFULLY_DELETED;
            } catch (HibernateException e) {
                log.info(e.getMessage());
            }
            return MyResponse.CANT_DELETE;
        }
        return MyResponse.INPUT_NOT_FOUND;
    }

    private void changeMaterials(Set<ProductList> materials, double amount, char type) {
        materials.forEach(productList -> {
            final WareHouse material = productList.getMaterial();

            if (type == ConstProperties.OPERATOR_PLUS) {
                material.setAmount(material.getAmount() + productList.getAmount() * amount);
            } else {
                material.setAmount(material.getAmount() - productList.getAmount() * amount);
            }
            wareHouseRepository.save(material);
        });
    }

    private void changeMoneyWithKPI(Input input, char type) {
        if (input.getMaterial().getType().name().equals(ProductType.PRODUCT.name())) {
            User user = userRepository.findByFullName(input.getCreatedBy());

            if (type == ConstProperties.OPERATOR_MINUS) {
                user.setBalance(user.getBalance() - input.getAmount() * input.getMaterial().getPrice() * ConstProperties.WORKER_KPI);
            } else {
                user.setBalance(user.getBalance() + input.getAmount() * input.getMaterial().getPrice() * ConstProperties.WORKER_KPI);
            }
            userRepository.save(user);
        }
    }
}
