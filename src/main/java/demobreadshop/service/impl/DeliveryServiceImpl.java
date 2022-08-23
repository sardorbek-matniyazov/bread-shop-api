package demobreadshop.service.impl;

import demobreadshop.domain.Delivery;
import demobreadshop.domain.Output;
import demobreadshop.domain.WareHouse;
import demobreadshop.domain.enums.OutputType;
import demobreadshop.payload.DeliveryDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.repository.DeliveryRepository;
import demobreadshop.repository.OutputRepository;
import demobreadshop.repository.WareHouseRepository;
import demobreadshop.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository repository;
    private final OutputRepository outputRepository;
    private final WareHouseRepository productRepository;

    @Autowired
    public DeliveryServiceImpl(DeliveryRepository repository, OutputRepository outputRepository, WareHouseRepository productRepository) {
        this.repository = repository;
        this.outputRepository = outputRepository;
        this.productRepository = productRepository;
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

                delivery.setPocket(delivery.getPocket() + dto.getAmount());
                outputRepository.save(
                        new Output(
                                productRepository.save(product),
                                dto.getAmount(),
                                OutputType.O_DELIVERER,
                                repository.save(delivery)
                        )
                );
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
}
