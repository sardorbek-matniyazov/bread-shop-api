package demobreadshop.service;

import demobreadshop.domain.Delivery;
import demobreadshop.domain.Output;
import demobreadshop.domain.ProductList;
import demobreadshop.payload.DeliveryDto;
import demobreadshop.payload.MyResponse;

import java.util.List;
import java.util.Set;

public interface DeliveryService {
    List<Delivery> getAll();

    Delivery get(long id);

    MyResponse deliver(DeliveryDto dto);

    List<Output> getDeliveries(long id);

    Set<ProductList> getBalance(long id);

    List<Output> getAllDeliveries();

    MyResponse deleteOutputWithId(long id);

    MyResponse returnProduct();
}
