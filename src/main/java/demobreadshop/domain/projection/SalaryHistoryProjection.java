package demobreadshop.domain.projection;

import java.sql.Timestamp;

public interface SalaryHistoryProjection {
    Double getAmount();
    Timestamp getCreatedAt();
}
