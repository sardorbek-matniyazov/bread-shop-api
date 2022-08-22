package demobreadshop.service.impl;

import demobreadshop.domain.Delivery;
import demobreadshop.payload.DeliveryDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.repository.DeliveryRepository;
import demobreadshop.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository repository;

    @Autowired
    public DeliveryServiceImpl(DeliveryRepository repository) {
        this.repository = repository;
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
    public MyResponse deliver(DeliveryDto dto) {
        return null;
    }

    @Override
    public MyResponse delete(long id) {
        return null;
    }
}
