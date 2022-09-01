package demobreadshop.service.impl;

import demobreadshop.domain.*;
import demobreadshop.domain.enums.OutputType;
import demobreadshop.payload.DeliveryDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.repository.*;
import demobreadshop.service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository repository;
    private final OutputRepository outputRepository;
    private final WareHouseRepository productRepository;
    private final ProductListRepository productListRepository;

    private final InputRepository inputRepository;
    @Autowired
    public DeliveryServiceImpl(DeliveryRepository repository, OutputRepository outputRepository, WareHouseRepository productRepository, ProductListRepository productListRepository, InputRepository inputRepository) {
        this.repository = repository;
        this.outputRepository = outputRepository;
        this.productRepository = productRepository;
        this.productListRepository = productListRepository;
        this.inputRepository = inputRepository;
    }

    @Override
    public List<Delivery> getAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Delivery get(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public MyResponse deliver(DeliveryDto dto) {
        Optional<Delivery> byId = repository.findById(dto.getDeliveryId());
        if (byId.isPresent()) {
            Optional<WareHouse> byIdProduct = productRepository.findById(dto.getProductId());
            if (byIdProduct.isPresent()) {
                WareHouse product = byIdProduct.get();
                product.setAmount(product.getAmount() - dto.getAmount());
                if (product.getAmount() < 0) {
                    return MyResponse.INPUT_TYPE_ERROR;
                }

                Delivery delivery = byId.get();

                Output output = outputRepository.save(
                        new Output(
                                productRepository.save(product),
                                dto.getAmount(),
                                OutputType.O_DELIVERER,
                                repository.save(delivery)
                        )
                );
                addBalanceDelivery(delivery, output);
                return MyResponse.SUCCESSFULLY_DELIVERED;
            }
            return MyResponse.PRODUCT_NOT_FOUND;
        }
        return MyResponse.DELIVERY_NOT_FOUND;
    }

    @Override
    public List<Output> getDeliveries(long id) {
        return outputRepository.findAllByDeliveryId(id);
    }

    @Override
    public Set<ProductList> getBalance(long id) {
        Optional<Delivery> byId = repository.findById(id);
        if (byId.isPresent()) {
            Delivery delivery = byId.get();
            return delivery.getBalance();
        }
        return null;
    }

    @Override
    public List<Output> getAllDeliveries() {
        return outputRepository.findAllByTypeOrderByCreatedAtDesc(OutputType.O_DELIVERER);
    }

    @Override
    public MyResponse deleteOutputWithId(long id) {
        Optional<Output> byId = outputRepository.findById(id);
        if (byId.isPresent()) {
            Output output = byId.get();
            WareHouse product = output.getMaterial();

            // checking is non deletable
            if (AuthServiceImpl.isNonDeletable(output.getCreatedAt().getTime())) {
                return MyResponse.CANT_DELETE;
            }

            // changing balance and checking product is exists
            if (!changeDeliveryBalance(output.getDelivery().getDeliverer().getId(), output)) {
                return MyResponse.YOU_DONT_HAVE_THIS_PRODUCT;
            }

            product.setAmount(product.getAmount() + output.getAmount());
            productRepository.save(product);
            outputRepository.delete(output);

            return MyResponse.SUCCESSFULLY_DELETED;
        }
        return MyResponse.DELIVERY_NOT_FOUND;
    }

    @Override
    public MyResponse returnProduct() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Delivery delivery = repository.findByDelivererId(user.getId());
        Set<ProductList> balance;

        try {
            balance = delivery.getBalance();
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            return MyResponse.YOU_DONT_HAVE_THIS_PRODUCT;
        }

        balance.forEach(e -> {
            WareHouse product = e.getMaterial();
            product.setAmount(product.getAmount() + e.getAmount());
            inputRepository.save(
                    new Input(
                            productRepository.save(product),
                            e.getAmount(),
                            product.getType()
                    )
            );

            productListRepository.delete(e);
        });

        delivery.setBalance(new HashSet<>());
        repository.save(delivery);
        return MyResponse.SUCCESSFULLY_CREATED;
    }

    public void addBalanceDelivery(Delivery delivery, Output output) {
        AtomicBoolean isExist = new AtomicBoolean(false);
        changeBalanceDelivery(delivery, output, isExist, productListRepository, repository);
    }

    static void changeBalanceDelivery(Delivery delivery, Output output, AtomicBoolean isExist, ProductListRepository productListRepository, DeliveryRepository repository) {
        delivery.getBalance().forEach(e -> {
            if (Objects.equals(e.getMaterial().getId(), output.getMaterial().getId())) {
                isExist.set(true);
                e.setAmount(e.getAmount() + output.getAmount());
                productListRepository.save(e);
            }
        });
        if (!isExist.get()) {
            Set<ProductList> balance = delivery.getBalance();
            balance.add(
                    productListRepository.save(
                            new ProductList(
                                    output.getMaterial(),
                                    output.getAmount()
                            )
                    )
            );
        }
        repository.save(delivery);
    }

    private boolean changeDeliveryBalance(Long delivererId, Output output) {
        return divideAmountOfProductInDelivery(delivererId, output, repository, productListRepository);
    }

    static boolean divideAmountOfProductInDelivery(Long delivererId, Output output, DeliveryRepository repository, ProductListRepository productListRepository) {
        AtomicBoolean isExist = new AtomicBoolean(false);
        Delivery delivery = repository.findByDelivererId(delivererId);
        Set<ProductList> balance = new HashSet<>();
        AtomicLong prId = new AtomicLong(0L);
        delivery.getBalance().forEach(e -> {
            if (Objects.equals(e.getMaterial().getId(), output.getMaterial().getId())) {
                isExist.set(true);
                if (e.getAmount() - output.getAmount() == 0) {
                    prId.set(e.getId());
                } else if (e.getAmount() - output.getAmount() < 0) {
                    isExist.set(false);
                } else {
                    e.setAmount(e.getAmount() - output.getAmount());
                    productListRepository.save(e);
                    balance.add(e);
                }
            } else {
                balance.add(e);
            }
        });
        if (!isExist.get()) {
            return false;
        }
        delivery.setBalance(balance);
        repository.save(delivery);
        if (prId.get() != 0) {
            productListRepository.deleteById(prId.get());
        }
        return isExist.get();
    }

}

// published by Sardorbek Matniyazov