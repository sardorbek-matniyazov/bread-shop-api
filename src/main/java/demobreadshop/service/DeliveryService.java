package demobreadshop.service;

import demobreadshop.domain.Delivery;
import demobreadshop.domain.Input;
import demobreadshop.domain.Output;
import demobreadshop.domain.ProductList;
import demobreadshop.payload.DeliveryDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.ProductListDto;

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

    Set<ProductList> getCurrentBalance();

    List<Input> getAllReturns(Long id);

    MyResponse confirmReturnedProduct(Long inputId);

    List<Input> getAllWaitReturns(Long id);

    MyResponse returnSelectedProduct(ProductListDto dto);

    MyResponse cancelReturnedProduct(Long inputId);
}
