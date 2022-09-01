package demobreadshop.domain.projection;

import java.sql.Timestamp;

public interface SalaryHistoryProjection {
    String getFullName();
    Double getUserKpi();
    Double getAmount();
    Double getAllSum();
    Timestamp getCreatedAt();
}
