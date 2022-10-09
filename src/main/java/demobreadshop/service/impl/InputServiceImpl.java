package demobreadshop.service.impl;

import demobreadshop.constants.ConstProperties;
import demobreadshop.domain.*;
import demobreadshop.domain.enums.ProductType;
import demobreadshop.domain.enums.RoleName;
import demobreadshop.payload.InputDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.WorkerAccessDto;
import demobreadshop.repository.InputRepository;
import demobreadshop.repository.UserRepository;
import demobreadshop.repository.WareHouseRepository;
import demobreadshop.repository.WorkerTourniquetRepository;
import demobreadshop.service.InputService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class InputServiceImpl implements InputService {

    private final InputRepository repository;
    private final WareHouseRepository wareHouseRepository;
    private final UserRepository userRepository;
    private final WorkerTourniquetRepository workerTourniquetRepository;

    @Autowired
    public InputServiceImpl(InputRepository repository, WareHouseRepository wareHouseRepository, UserRepository userRepository, WorkerTourniquetRepository workerTourniquetRepository) {
        this.repository = repository;
        this.wareHouseRepository = wareHouseRepository;
        this.userRepository = userRepository;
        this.workerTourniquetRepository = workerTourniquetRepository;
    }

    @Override
    public List<Input> getAll() {
        return repository.findAllByTypeOrderByIdDesc(ProductType.PRODUCT);
    }

    @Override
    public Input get(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Input> getAllWarehouseInputs() {
        return repository.findAllByTypeOrderByIdDesc(ProductType.MATERIAL);
    }

    @Override
    public MyResponse create(InputDto dto) {
        final Optional<WareHouse> byId = wareHouseRepository.findById(dto.getProductId());
        if (byId.isPresent()) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            User userTwo = user;

            final WareHouse product = byId.get();
            if (product.getType().equals(ProductType.PRODUCT)) {
                if (dto.getWorkerOneId() == null || dto.getWorkerTwoId() == null) {
                    return MyResponse.INPUT_TYPE_ERROR;
                }
                Optional<User> byId1;
                if (user.getId().equals(dto.getWorkerOneId())) {
                    byId1 = userRepository.findById(dto.getWorkerTwoId());
                } else if (user.getId().equals(dto.getWorkerTwoId())) {
                    byId1 = userRepository.findById(dto.getWorkerOneId());
                } else return MyResponse.YOU_HAVEN_T_ACCESS;

                if (byId1.isPresent()) {
                    userTwo = byId1.get();
                } else return MyResponse.WORKER_NOT_FOUND;
            }

            if (user.getRoles().stream().anyMatch(r -> r.getRoleName().equals(RoleName.GL_ADMIN)) && product.getType().equals(ProductType.PRODUCT)) {
                return MyResponse.YOU_CANT_CREATE;
            }

            if (product.getType().equals(ProductType.PRODUCT) && (!user.getUserAccess() || !userTwo.getUserAccess())) {
                return MyResponse.YOU_HAVEN_T_ACCESS;
            }

            product.setAmount(product.getAmount() + dto.getAmount());
            changeMaterials(product.getMaterials(), dto.getAmount(), ConstProperties.OPERATOR_MINUS);

            Double benefitWithWarehouseId = wareHouseRepository.findBenefitWithWarehouseId(product.getId());
            Input input = repository.save(
                    new Input(
                            wareHouseRepository.save(product),
                            dto.getAmount() / 2,
                            product.getType(),
                            user.getUserKPI(),
                            product.getPrice(),
                            benefitWithWarehouseId == null ? 0.0 : benefitWithWarehouseId
                    )
            );
            changeMoneyWithKPI(input, ConstProperties.OPERATOR_PLUS);

            if (input.getType().equals(ProductType.PRODUCT)) {
                authenticateUser(userTwo);
                input = repository.save(
                        new Input(
                                wareHouseRepository.save(product),
                                dto.getAmount() / 2,
                                product.getType(),
                                user.getUserKPI(),
                                product.getPrice(),
                                benefitWithWarehouseId == null ? 0.0 : benefitWithWarehouseId
                        )
                );
                changeMoneyWithKPI(input, ConstProperties.OPERATOR_PLUS);
            }
            // done
            authenticateUser(user);
            return MyResponse.SUCCESSFULLY_CREATED;
        }
        return MyResponse.PRODUCT_NOT_FOUND;
    }

    private void authenticateUser(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
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

    @Override
    @Transactional
    public MyResponse setAdminAccess(WorkerAccessDto dto) {
        Optional<User> adminOneById = userRepository.findById(dto.getAdminOneId());
        Optional<User> adminTwoById = userRepository.findById(dto.getAdminTwoId());

        if (adminOneById.isPresent() && adminTwoById.isPresent()) {
            User adminOne = adminOneById.get();
            User adminTwo = adminTwoById.get();

            if (adminOne.getRoles().stream().noneMatch(r -> r.getRoleName().equals(RoleName.WORKER))) {
                return MyResponse.ARENT_WORKER;
            }

            if (adminTwo.getRoles().stream().noneMatch(r -> r.getRoleName().equals(RoleName.WORKER))) {
                return MyResponse.ARENT_WORKER;
            }

            userRepository.setAccessUserFalse();
            workerTourniquetRepository.setEndedDate(Timestamp.valueOf(LocalDateTime.now()));

            userRepository.setAccessUser(adminOne.getId(), adminTwo.getId());
            workerTourniquetRepository.save(
                    new WorkerTourniquet(
                            adminOne,
                            adminTwo
                    )
            );

            return MyResponse.SUCCESSFULLY_UPDATED;
        }

        return MyResponse.USER_NOT_FOUND;
    }

    @Override
    @Transactional
    public MyResponse setAdminNonAccess() {

        userRepository.setAccessUserFalse();
        workerTourniquetRepository.setEndedDate(Timestamp.valueOf(LocalDateTime.now()));

        return MyResponse.SUCCESSFULLY_UPDATED;
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

            if (user.getRoles().stream().anyMatch(role -> role.getRoleName().equals(RoleName.WORKER))) {
                if (type == ConstProperties.OPERATOR_MINUS) {
                    user.setBalance(user.getBalance() - input.getAmount() * user.getUserKPI());
                } else {
                    user.setBalance(user.getBalance() + input.getAmount() * user.getUserKPI());
                }
                userRepository.save(user);
            }
        }
    }
}
