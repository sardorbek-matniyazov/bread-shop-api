package demobreadshop.service;

import demobreadshop.domain.PayArchive;
import demobreadshop.domain.Sale;
import demobreadshop.domain.enums.Status;
import demobreadshop.domain.projection.SalaryHistoryProjection;
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

    List<Sale> getAllByType(Status debt);

    List<SalaryHistoryProjection> getSalaryHistory(long id);
}
