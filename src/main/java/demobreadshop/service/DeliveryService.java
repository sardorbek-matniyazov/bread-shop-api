package demobreadshop.service;

import demobreadshop.domain.Delivery;
import demobreadshop.payload.DeliveryDto;
import demobreadshop.payload.MyResponse;

import java.util.List;

public interface DeliveryService {
    List<Delivery> getAll();

    Delivery get(long id);

    MyResponse deliver(DeliveryDto dto);

    MyResponse delete(long id);
}
