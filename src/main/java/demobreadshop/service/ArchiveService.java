package demobreadshop.service;

import demobreadshop.domain.Outcome;
import demobreadshop.domain.Sale;
import demobreadshop.domain.enums.ProductType;
import demobreadshop.domain.enums.RoleName;
import demobreadshop.domain.projection.*;

import java.util.List;
import java.util.Map;

public interface ArchiveService {
    Map<String, Double> getAll();

    Map<String, Double> outcomeStat(String start, String end);

    List<SaleStatistics> getAllSellerInfo(RoleName sellerAdmin, String start, String end);

    List<MaterialDecreaseStat> getAllMaterialDecrease(String start, String end);

    List<GroupStatistics> getAllGroupStatistics(String start, String end);

    List<InputStatistics> getAllProductStatistics(ProductType material, String start, String end);

    List<InputStatistics> getAllWarehouseStatistics(String start, String end);

    List<SellerStatistics> getAllSellerStatistics(String start, String end);

    List<ClientStatistics> clientStat(String start, String end);

    List<SaleInfoProjection> getSaleInfo(String start, String end);

    List<SalaryHistoryProjection> getAllIncomeHistoryInfo(Long id, String start, String end);

    List<AllClientIncomeProjection> getAllClientIncome(String start, String end);

    Map<String, Double> getFinanceInfo(String start, String end);

    List<Outcome> getAllOutcomeHistoryInfo(Long id, String start, String end);

    Map<String, Object> getAllSellerSaleInfo(Long id, String start, String end);

    List<ClientSumStatistics> getAllClientPaidSum(String start, String end);

    List<Sale> getAllClientSaleInfo(Long id, String start, String end);

    Map<String, Object> getAllBenefits(String start, String end);

    Map<String, Object> getAllSale(String start, String end);

    List<ClientsDebt> getAllClientsDebt();
}
