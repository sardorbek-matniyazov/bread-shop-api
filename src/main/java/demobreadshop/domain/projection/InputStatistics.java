package demobreadshop.domain.projection;

import java.sql.Timestamp;

public interface InputStatistics {
    Long getProductId();
    Double getAmount();
    String getName();
    Timestamp getUpdatedAt();
}
