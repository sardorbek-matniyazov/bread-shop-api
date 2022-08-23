package demobreadshop.service.impl;

import demobreadshop.domain.Delivery;
import demobreadshop.domain.Output;
import demobreadshop.domain.ProductList;
import demobreadshop.domain.WareHouse;
import demobreadshop.domain.enums.OutputType;
import demobreadshop.payload.DeliveryDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.repository.DeliveryRepository;
import demobreadshop.repository.OutputRepository;
import demobreadshop.repository.ProductListRepository;
import demobreadshop.repository.WareHouseRepository;
import demobreadshop.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository repository;
    private final OutputRepository outputRepository;
    private final WareHouseRepository productRepository;
    private final ProductListRepository productListRepository;

    @Autowired
    public DeliveryServiceImpl(DeliveryRepository repository, OutputRepository outputRepository, WareHouseRepository productRepository, ProductListRepository productListRepository) {
        this.repository = repository;
        this.outputRepository = outputRepository;
        this.productRepository = productRepository;
        this.productListRepository = productListRepository;
    }

    @Override
    public List<Delivery> getAll() {
        return repository.findAll();
    }

    @Override
    public Delivery get(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public MyResponse deliver(DeliveryDto dto)  {
        Optional<Delivery> byId = repository.findById(dto.getDeliveryId());
        if (byId.isPresent()) {
            Optional<WareHouse> byIdProduct = productRepository.findById(dto.getProductId());
            if (byIdProduct.isPresent()) {
                WareHouse product = byIdProduct.get();
                product.setAmount(product.getAmount() - dto.getAmount());
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
    public MyResponse delete(long id) {
        return null;
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

}
