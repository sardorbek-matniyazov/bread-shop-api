package demobreadshop.service.impl;

import demobreadshop.constants.ConstProperties;
import demobreadshop.domain.*;
import demobreadshop.domain.enums.*;
import demobreadshop.payload.DebtDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.SaleDto;
import demobreadshop.repository.*;
import demobreadshop.service.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class SaleServiceImpl implements SaleService {

    private final SaleRepository repository;
    private final ClientRepository clientRepository;
    private final WareHouseRepository productRepository;
    private final OutputRepository outputRepository;
    private final PayArchiveRepository archiveRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final ProductListRepository productListRepository;

    @Autowired
    public SaleServiceImpl(SaleRepository repository, ClientRepository clientRepository, WareHouseRepository productRepository, OutputRepository outputRepository, PayArchiveRepository archiveRepository, UserRepository userRepository, DeliveryRepository deliveryRepository, ProductListRepository productListRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.outputRepository = outputRepository;
        this.archiveRepository = archiveRepository;
        this.userRepository = userRepository;
        this.deliveryRepository = deliveryRepository;
        this.productListRepository = productListRepository;
    }

    @Override
    public List<Sale> getAll() {
        return repository.findAll();
    }

    @Override
    public Sale get(long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public MyResponse sell(SaleDto dto) {
        final Optional<Client> byId = clientRepository.findById(dto.getClientId());
        if (byId.isPresent()) {
            final Optional<WareHouse> byId1 = productRepository.findById(dto.getProductId());
            if (byId1.isPresent()) {
                final Client client = byId.get();

                final WareHouse product = byId1.get();

                double wholePrice = product.getPrice() * dto.getAmount();
                double debtPrice = product.getPrice() * dto.getAmount() - dto.getCostCash() - dto.getCostCard();

                if (debtPrice < 0) {
                    return MyResponse.INPUT_TYPE_ERROR;
                }

                Output output = new Output(
                        product,
                        dto.getAmount(),
                        OutputType.O_SALE
                );

                Sale sale = new Sale(
                        outputRepository.save(output),
                        client,
                        wholePrice,
                        debtPrice,
                        debtPrice == 0 ? Status.PAYED : Status.DEBT
                );

                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (user.getRoles().stream().anyMatch(role -> role.getRoleName().equals(RoleName.SELLER_CAR))) {
                    sale.setSaleType(SaleType.SALE_CAR);
                    if (!changeDeliveryBalance(user.getId(), dto)) {
                        return MyResponse.YOU_DONT_HAVE_THIS_PRODUCT;
                    }
                } else {
                    sale.setSaleType(SaleType.SALE_ADMIN);
                    product.setAmount(product.getAmount() - dto.getAmount());
                    productRepository.save(product);
                }

                sale = createPaymentArchive(sale, dto.getCostCard(), dto.getCostCash());

                changeMoneyWithKPI(sale, ConstProperties.OPERATOR_PLUS);
                return MyResponse.SUCCESSFULLY_CREATED;
            }
            return MyResponse.PRODUCT_NOT_FOUND;
        }
        return MyResponse.CLIENT_NOT_FOUND;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public MyResponse delete(long id) {
        final Optional<Sale> byId = repository.findById(id);
        if (byId.isPresent()) {
            try {
                final Sale sale = byId.get();
                if (AuthServiceImpl.isNonDeletable(sale.getCreatedAt().getTime())) {
                    return MyResponse.CANT_DELETE;
                }

                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (!sale.getCreatedBy().equals(user.getFullName())) {
                    return MyResponse.CAN_DELETE_OWN;
                }
                if (sale.getSaleType().name().equals(SaleType.SALE_CAR.name())) {
                    rollbackChangesInDeliveryBalance(user.getId(), sale.getOutput());
                }
                WareHouse material = sale.getOutput().getMaterial();
                material.setAmount(material.getAmount() + sale.getOutput().getAmount());
                productRepository.save(material);

                repository.delete(sale);
                outputRepository.delete(sale.getOutput());

                changeMoneyWithKPI(sale, ConstProperties.OPERATOR_MINUS);
                return MyResponse.SUCCESSFULLY_DELETED;
            } catch (Exception e) {
                return MyResponse.CANT_DELETE;
            }
        }
        return MyResponse.SALE_NOT_FOUND;
    }

    @Override
    public List<PayArchive> getArchives(long id) {
        return archiveRepository.findAllBySaleId(id);
    }

    @Transactional
    @Override
    public MyResponse payForDebt(DebtDto dto) {
        Optional<Sale> byId = repository.findById(dto.getSaleId());
        if (byId.isPresent()) {
            Sale sale = byId.get();
            double currentDebt = sale.getDebtPrice() - dto.getCostCard() - dto.getCostCash();
            if (currentDebt < 0) {
                return MyResponse.INPUT_TYPE_ERROR;
            }
            if (currentDebt == 0) {
                sale.setType(Status.PAYED);
            }
            createPaymentArchive(sale, dto.getCostCard(), dto.getCostCash());

            sale.setDebtPrice(currentDebt);
            repository.save(sale);
            return MyResponse.SUCCESSFULLY_PAYED;
        }
        return MyResponse.SALE_NOT_FOUND;
    }

    @Override
    public List<Sale> getAllByType(Status debt) {
        return repository.findAllByType(Status.DEBT);
    }

    private Sale createPaymentArchive(Sale sale, double costCard, double costCash) {
        sale = repository.save(sale);
        if (costCard != 0.0) {
            archiveRepository.save(
                    new PayArchive(
                            costCard,
                            PayType.CARD,
                            sale
                    )
            );
        }
        if (costCash != 0.0) {
            archiveRepository.save(
                    new PayArchive(
                            costCard,
                            PayType.CASH,
                            sale
                    )
            );
        }
        return sale;
    }

    @ExceptionHandler(value = HibernateError.class)
    public static MyResponse handleHibernateException(HibernateError ex) {
        log.error(ex.getMessage());
        return MyResponse.CANT_DELETE;
    }

    private void changeMoneyWithKPI(Sale sale, char type) {
        User user = userRepository.findByFullName(sale.getCreatedBy());
        if (sale.getSaleType().name().equals(SaleType.SALE_CAR.name())) {
            if (type == ConstProperties.OPERATOR_MINUS) {
                user.setBalance(user.getBalance() - sale.getWholePrice() * ConstProperties.SELLER_CAR_KPI);
            } else {
                user.setBalance(user.getBalance() + sale.getWholePrice() * ConstProperties.SELLER_CAR_KPI);
            }
        } else if (sale.getSaleType().name().equals(SaleType.SALE_ADMIN.name())) {
            if (type == ConstProperties.OPERATOR_MINUS) {
                user.setBalance(user.getBalance() - sale.getWholePrice() * ConstProperties.SELLER_ADMIN_KPI);
            } else {
                user.setBalance(user.getBalance() + sale.getWholePrice() * ConstProperties.SELLER_ADMIN_KPI);
            }
        }
        userRepository.save(user);
    }

    private boolean changeDeliveryBalance(Long delivererId, SaleDto dto) {
        AtomicBoolean isExist = new AtomicBoolean(false);
        Delivery delivery = deliveryRepository.findByDelivererId(delivererId);
        Set<ProductList> balance = new HashSet<>();
        AtomicLong prId = new AtomicLong(0L);
        delivery.getBalance().forEach(e -> {
            if (e.getMaterial().getId() == dto.getProductId()) {
                isExist.set(true);
                if (e.getAmount() - dto.getAmount() <= 0) {
                    prId.set(e.getId());
                } else {
                    e.setAmount(e.getAmount() - dto.getAmount());
                    productListRepository.save(e);
                    balance.add(e);
                }
            } else {
                balance.add(e);
            }
        });
        delivery.setBalance(balance);
        if (prId.get() != 0) {
            productListRepository.deleteById(prId.get());
        }
        return isExist.get();
    }

    private void rollbackChangesInDeliveryBalance(Long delivererId, Output output) {
        AtomicBoolean isExist = new AtomicBoolean(false);
        Delivery delivery = deliveryRepository.findByDelivererId(delivererId);
        DeliveryServiceImpl.changeBalanceDelivery(delivery, output, isExist, productListRepository, deliveryRepository);
    }
}
