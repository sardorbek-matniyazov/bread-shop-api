package demobreadshop.service;

import demobreadshop.domain.enums.RoleName;
import demobreadshop.domain.projection.MaterialDecreaseStat;
import demobreadshop.domain.projection.SaleStatistics;

import java.util.List;
import java.util.Map;

public interface ArchiveService {
    Map<String, Double> getAll();

    Map<String, Double> outcomeStat();

    List<SaleStatistics> getAllCarSellerInfo(RoleName sellerAdmin);

    List<MaterialDecreaseStat> getAllMaterialDecrease();
}
