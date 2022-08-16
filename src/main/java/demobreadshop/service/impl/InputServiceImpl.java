package demobreadshop.service.impl;

import demobreadshop.domain.Input;
import demobreadshop.domain.ProductList;
import demobreadshop.domain.WareHouse;
import demobreadshop.domain.enums.ProductType;
import demobreadshop.payload.InputDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.repository.InputRepository;
import demobreadshop.repository.WareHouseRepository;
import demobreadshop.service.InputService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public InputServiceImpl(InputRepository repository, WareHouseRepository wareHouseRepository) {
        this.repository = repository;
        this.wareHouseRepository = wareHouseRepository;
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
            divideMaterials(product.getMaterials(), dto.getAmount());

            repository.save(
                    new Input(
                            wareHouseRepository.save(product),
                            dto.getAmount(),
                            product.getType()
                    )
            );
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
                repository.deleteById(id);

                // back up changes
                final WareHouse product = wareHouseRepository.getById(input.getMaterial().getId());
                product.setAmount(product.getAmount() - input.getAmount());
                addMaterials(product.getMaterials(), input.getAmount());

                wareHouseRepository.save(product);

                return MyResponse.SUCCESSFULLY_DELETED;
            } catch (HibernateException e) {
                log.info(e.getMessage());
            }
            return MyResponse.CANT_DELETE;
        }
        return MyResponse.INPUT_NOT_FOUND;
    }

    private void addMaterials(Set<ProductList> materials, double amount) {
        materials.forEach(productList -> {
            final WareHouse material = productList.getMaterial();
            material.setAmount(material.getAmount() + productList.getAmount() * amount);
            wareHouseRepository.save(material);
        });
    }

    private void divideMaterials(Set<ProductList> materials, double amount) {
        materials.forEach(productList -> {
            final WareHouse material = productList.getMaterial();
            material.setAmount(material.getAmount() - productList.getAmount() * amount);
            wareHouseRepository.save(material);
        });
    }

}
