package demobreadshop.service;

import demobreadshop.domain.WareHouse;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.ProductDto;

import java.util.List;

public interface ProductService {
    List<WareHouse> getAll();

    List<WareHouse> getAllWarehouseProducts();

    MyResponse create(ProductDto dto);

    WareHouse get(long id);

    MyResponse update(long id, ProductDto dto);
}
