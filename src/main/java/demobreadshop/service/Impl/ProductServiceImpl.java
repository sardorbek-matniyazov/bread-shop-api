package demobreadshop.service.Impl;

import demobreadshop.domain.ProductList;
import demobreadshop.domain.WareHouse;
import demobreadshop.domain.enums.ProductType;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.ProductDto;
import demobreadshop.payload.ProductListDto;
import demobreadshop.repository.WareHouseRepository;
import demobreadshop.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {
    private final WareHouseRepository repository;

    public ProductServiceImpl(WareHouseRepository wareHouseRepository) {
        this.repository = wareHouseRepository;
    }

    @Override
    public List<WareHouse> getAll() {
        return repository.findAllByType(ProductType.PRODUCT);
    }

    @Override
    public List<WareHouse> getAllWarehouseProducts() {
        return repository.findAllByType(ProductType.MATERIAL);
    }

    @Override
    public MyResponse create(ProductDto dto) {
        if (repository.existsByName(dto.getName())) {
            return MyResponse.MATERIAL_NAME_EXISTS;
        }
        if (dto.getProductType().equals(ProductType.PRODUCT.name())) {
            WareHouse product = new WareHouse(
                    dto.getName(),
                    dto.getPrice(),
                    dto.getDescription(),
                    ProductType.PRODUCT
            );

            product.setMaterials(makeMaterials(dto.getMaterials()));

            repository.save(product);
        } else {
            repository.save(
                    new WareHouse(
                            dto.getName(),
                            dto.getPrice(),
                            dto.getDescription(),
                            ProductType.MATERIAL
                    )
            );
        }
        return MyResponse.SUCCESSFULLY_CREATED;
    }

    @Override
    public WareHouse get(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public MyResponse update(long id, ProductDto dto) {
        if (repository.existsByNameAndIdIsNot(dto.getName(), id)) {
            return MyResponse.MATERIAL_NAME_EXISTS;
        }
        final Optional<WareHouse> repositoryById = repository.findById(id);
        if (repositoryById.isPresent()) {
            WareHouse product = repositoryById.get();
            product.setName(dto.getName());
            product.setPrice(dto.getPrice());
            product.setDescription(dto.getDescription());

            if (product.getMaterials() != null) {
                product.setMaterials(makeMaterials(dto.getMaterials()));
            }

            repository.save(product);
            return MyResponse.SUCCESSFULLY_UPDATED;
        }
        return MyResponse.PRODUCT_NOT_FOUND;
    }

    private Set<ProductList> makeMaterials(List<ProductListDto> materials) {
        Set<ProductList> materialList = new HashSet<>();
        materials.forEach(
                m -> {
                    final Optional<WareHouse> byId = repository.findById(m.getMaterialId());
                    if (byId.isPresent()) {
                        materialList.add(
                                new ProductList(
                                        repository.getById(m.getMaterialId()),
                                        m.getAmount(),
                                        m.getDescription()
                                )
                        );
                    }
                }
        );
        return materialList;
    }
}
