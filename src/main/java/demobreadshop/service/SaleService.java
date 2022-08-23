package demobreadshop.service;

import demobreadshop.domain.PayArchive;
import demobreadshop.domain.Sale;
import demobreadshop.payload.DebtDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.SaleDto;

import java.util.List;

public interface SaleService {
    List<Sale> getAll();

    Sale get(long id);

    MyResponse sell(SaleDto dto);

    MyResponse delete(long id);

    List<PayArchive> getArchives(long id);

    MyResponse payForDebt(DebtDto dto);
}
