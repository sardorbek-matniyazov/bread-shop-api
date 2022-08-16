package demobreadshop.service.impl;

import demobreadshop.domain.ProductList;
import demobreadshop.domain.WareHouse;
import demobreadshop.domain.enums.ProductType;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.ProductDto;
import demobreadshop.payload.ProductListDto;
import demobreadshop.repository.ProductListRepository;
import demobreadshop.repository.WareHouseRepository;
import demobreadshop.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final WareHouseRepository repository;
    private final ProductListRepository productListRepository;

    @Autowired
    public ProductServiceImpl(WareHouseRepository wareHouseRepository, ProductListRepository productListRepository) {
        this.repository = wareHouseRepository;
        this.productListRepository = productListRepository;
    }

    @Override
    public List<WareHouse> getAll() {
        return repository.findAllByType(ProductType.PRODUCT);
    }

    @Override
    public List<WareHouse> getAllWarehouseProducts() {
        return repository.findAllByType(ProductType.MATERIAL);
    }

    @Transactional
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

    @Transactional
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

    @Transactional
    @Override
    public MyResponse delete(long id) {
        final Optional<WareHouse> byId = repository.findById(id);
        if (byId.isPresent()) {
            try {
                repository.deleteById(id);
                return MyResponse.SUCCESSFULLY_DELETED;
            } catch (HibernateException e) {
                log.info(e.getMessage());
                return MyResponse.CANT_DELETE;
            }
        }
        return MyResponse.PRODUCT_NOT_FOUND;
    }

    @Override
    public Set<ProductList> getMaterials(long id) {
        final Optional<WareHouse> byId = repository.findById(id);
        if (byId.isPresent()) {
            final WareHouse product = byId.get();
            return product.getMaterials();
        }
        return null;
    }

    private Set<ProductList> makeMaterials(List<ProductListDto> materials) {
        Set<ProductList> materialList = new HashSet<>();
        materials.forEach(
                m -> {
                    final Optional<WareHouse> byId = repository.findById(m.getMaterialId());
                    if (byId.isPresent()) {
                        materialList.add(
                                productListRepository.save(
                                        new ProductList(
                                            repository.getById(m.getMaterialId()),
                                            m.getAmount(),
                                            m.getDescription()
                                        )
                                )
                        );
                    }
                }
        );
        return materialList;
    }
}
