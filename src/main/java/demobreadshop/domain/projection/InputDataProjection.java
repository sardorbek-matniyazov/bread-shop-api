package demobreadshop.domain.projection;

import java.sql.Timestamp;

public interface InputDataProjection {
    Long getId();
    String getProduct();
    Double getAmount();
    String getCreatedBy();
    Timestamp getCreatedAt();
}
