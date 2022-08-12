package demobreadshop.service.Impl;

import demobreadshop.domain.Input;
import demobreadshop.domain.WareHouse;
import demobreadshop.domain.enums.ProductType;
import demobreadshop.payload.InputDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.repository.InputRepository;
import demobreadshop.repository.WareHouseRepository;
import demobreadshop.service.InputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
        if (byId.isPresent()){
            final WareHouse product = byId.get();
            repository.save(
                    new Input(
                            product,
                            dto.getAmount(),
                            product.getType()
                    )
            );
            return MyResponse.SUCCESSFULLY_CREATED;
        }
        return MyResponse.PRODUCT_NOT_FOUND;
    }
}
