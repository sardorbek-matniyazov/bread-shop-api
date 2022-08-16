package demobreadshop.service;

import demobreadshop.domain.Sale;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.SaleDto;

import java.util.List;

public interface SaleService {
    List<Sale> getAll();

    Sale get(long id);

    MyResponse sell(SaleDto dto);

    MyResponse delete(long id);
}
