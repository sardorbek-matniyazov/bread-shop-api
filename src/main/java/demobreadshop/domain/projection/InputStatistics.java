package demobreadshop.domain.projection;

import java.sql.Timestamp;

public interface InputStatistics {
    Long getProductId();
    Double getAmount();
    String getName();
    Double getPrice();
    Double getWholePrice();
    Timestamp getUpdatedAt();
}
