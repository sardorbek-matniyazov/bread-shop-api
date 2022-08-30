package demobreadshop.service;

import demobreadshop.domain.enums.RoleName;
import demobreadshop.domain.projection.*;

import java.util.List;
import java.util.Map;

public interface ArchiveService {
    Map<String, Double> getAll();

    Map<String, Double> outcomeStat();

    List<SaleStatistics> getAllSellerInfo(RoleName sellerAdmin);

    List<MaterialDecreaseStat> getAllMaterialDecrease();

    List<GroupStatistics> getAllGroupStatistics();

    List<InputStatistics> getAllProductStatistics();

    List<InputStatistics> getAllWarehouseStatistics();

    List<SellerStatistics> getAllSellerStatistics();

    List<ClientStatistics> clientStat();

    List<SaleInfoProjection> getSaleInfo();

}
