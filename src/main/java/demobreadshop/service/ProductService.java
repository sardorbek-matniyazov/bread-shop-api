package demobreadshop.service;

import demobreadshop.domain.ProductList;
import demobreadshop.domain.WareHouse;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.ProductDto;

import java.util.List;
import java.util.Set;

public interface ProductService {
    List<WareHouse> getAll();

    List<WareHouse> getAllWarehouseProducts();

    MyResponse create(ProductDto dto);

    WareHouse get(long id);

    MyResponse update(long id, ProductDto dto);

    MyResponse delete(long id);

    Set<ProductList> getMaterials(long id);
}
