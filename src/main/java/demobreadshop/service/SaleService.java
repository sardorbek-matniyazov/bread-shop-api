package demobreadshop.service;

import demobreadshop.domain.PayArchive;
import demobreadshop.domain.Sale;
import demobreadshop.domain.enums.PaymentStatus;
import demobreadshop.domain.enums.SaleStatus;
import demobreadshop.payload.DebtDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.PaymentDateDto;
import demobreadshop.payload.SaleDto;

import java.util.List;

public interface SaleService {
    List<Sale> getAll();

    Sale get(long id);

    MyResponse sell(SaleDto dto);

    MyResponse delete(long id);

    List<PayArchive> getArchives(long id);

    MyResponse payForDebt(DebtDto dto);

    List<Sale> getAllByType(SaleStatus debt, boolean isKindergarten);

    MyResponse checkPayment(Long id);

    List<PayArchive> getPaymentsByType(PaymentStatus wait);

    MyResponse updatePaymentDate(Long id, PaymentDateDto dto);

    MyResponse deletePayment(Long id);
}
