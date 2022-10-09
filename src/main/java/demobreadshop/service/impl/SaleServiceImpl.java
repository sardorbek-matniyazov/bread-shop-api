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
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static demobreadshop.service.impl.DeliveryServiceImpl.divideAmountOfProductInDelivery;

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
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
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
                if (client.isKindergarten()) {
                    wholePrice = product.getKindergartenPrice() * dto.getAmount();
                }
                double debtPrice = wholePrice - dto.getCostCash() - dto.getCostCard();

                if (debtPrice < 0) {
                    return MyResponse.INPUT_TYPE_ERROR;
                }

                Output output = new Output(
                        product,
                        dto.getAmount(),
                        OutputType.O_SALE
                );

                SaleType type;
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (user.getRoles().stream().anyMatch(role -> role.getRoleName().equals(RoleName.SELLER_CAR))) {
                    type = SaleType.SALE_CAR;
                    if (!changeDeliveryBalance(user.getId(), output)) {
                        return MyResponse.YOU_DONT_HAVE_THIS_PRODUCT;
                    }
                } else {
                    type = SaleType.SALE_ADMIN;

                    if (product.getAmount() < dto.getAmount()) {
                        return MyResponse.INPUT_TYPE_ERROR;
                    }
                    product.setAmount(product.getAmount() - dto.getAmount());
                    productRepository.save(product);
                }

                Sale sale = new Sale(
                        outputRepository.save(output),
                        client,
                        wholePrice,
                        debtPrice,
                        client.isKindergarten() ? product.getKindergartenPrice() : product.getPrice(),
                        debtPrice == 0 ? Status.PAYED : Status.DEBT,
                        user.getUserKPI()
                );

                sale.setSaleType(type);
                repository.save(sale);
                createPaymentArchive(sale, dto.getCostCard(), dto.getCostCash(), PaymentStatus.PAID);

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
                } else {
                    WareHouse material = sale.getOutput().getMaterial();
                    material.setAmount(material.getAmount() + sale.getOutput().getAmount());
                    productRepository.save(material);
                }

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
        return archiveRepository.findAllBySaleIdOrderByIdDesc(id);
    }

    @Transactional
    @Override
    public MyResponse payForDebt(DebtDto dto) {
        if (dto.getCostCash() == 0.0 && dto.getCostCard() == 0.0) {
            return MyResponse.INPUT_TYPE_ERROR;
        }
        Optional<Sale> byId = repository.findById(dto.getSaleId());
        if (byId.isPresent()) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Sale sale = byId.get();
            if (user.getRoles().stream().noneMatch(role -> role.getRoleName().equals(RoleName.SELLER_ADMIN) || role.getRoleName().equals(RoleName.GL_ADMIN))
                    && !sale.getCreatedBy().equals(user.getFullName())) {
                return MyResponse.YOU_CANT_CREATE;
            }

            Double allWaitSum = repository.sumOfDebtByStatusWait(dto.getSaleId());
            allWaitSum = allWaitSum== null ? 0.0 : allWaitSum;
            double currentDebt = sale.getDebtPrice() - dto.getCostCard() - dto.getCostCash() - allWaitSum;

            if (currentDebt < 0.0) {
                return MyResponse.INPUT_TYPE_ERROR;
            }
            if (currentDebt == 0.0) {
                sale.setType(Status.PAYED);
            }

            createPaymentArchive(sale, dto.getCostCard(), dto.getCostCash(), PaymentStatus.WAIT);
            return MyResponse.SUCCESSFULLY_PAYED;
        }
        return MyResponse.SALE_NOT_FOUND;
    }

    @Override
    public List<Sale> getAllByType(Status type, boolean isKindergarten) {
        return repository.findAllByTypeAndClient_IsKindergarten(type, isKindergarten, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    @Transactional
    public MyResponse checkPayment(Long id) {
        Optional<PayArchive> byId = archiveRepository.findById(id);
        if (byId.isPresent()) {
            PayArchive payArchive = byId.get();

            Long saleId = archiveRepository.findSaleId(id);
            repository.setDebtPriceWithPayArchive(saleId, payArchive.getAmount());
            payArchive.setStatus(PaymentStatus.PAID);
            archiveRepository.save(payArchive);
            return MyResponse.SUCCESSFULLY_UPDATED;
        }

        return MyResponse.PAYMENT_NOT_FOUND;
    }

    @Override
    public List<PayArchive> getPaymentsByType(PaymentStatus wait) {
        return archiveRepository.findAllByStatusOrderByIdDesc(wait);
    }

    private void createPaymentArchive(Sale sale, double costCard, double costCash, PaymentStatus status) {
        if (costCard != 0.0) {
            archiveRepository.save(
                    new PayArchive(
                            costCard,
                            PayType.CARD,
                            status,
                            sale
                    )
            );
        }
        if (costCash != 0.0) {
            archiveRepository.save(
                    new PayArchive(
                            costCash,
                            PayType.CASH,
                            status,
                            sale
                    )
            );
        }
    }

    @ExceptionHandler(value = HibernateError.class)
    public static MyResponse handleHibernateException(HibernateError ex) {
        log.error(ex.getMessage());
        return MyResponse.CANT_DELETE;
    }

    private void changeMoneyWithKPI(Sale sale, char type) {
        User user = userRepository.findByFullName(sale.getCreatedBy());
        if (type == ConstProperties.OPERATOR_MINUS) {
            user.setBalance(user.getBalance() - sale.getOutput().getAmount() * user.getUserKPI());
        } else {
            user.setBalance(user.getBalance() + sale.getOutput().getAmount() * user.getUserKPI());
        }
        userRepository.save(user);
    }

    private boolean changeDeliveryBalance(Long delivererId, Output output) {
        return divideAmountOfProductInDelivery(delivererId, output, deliveryRepository, productListRepository);
    }

    private void rollbackChangesInDeliveryBalance(Long delivererId, Output output) {
        AtomicBoolean isExist = new AtomicBoolean(false);
        Delivery delivery = deliveryRepository.findByDelivererId(delivererId);
        DeliveryServiceImpl.changeBalanceDelivery(delivery, output, isExist, productListRepository, deliveryRepository);
    }
}

// Published by Sardorbek Matniyazov 09.2022