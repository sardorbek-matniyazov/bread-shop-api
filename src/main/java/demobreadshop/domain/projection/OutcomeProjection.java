package demobreadshop.domain.projection;

import java.sql.Timestamp;

public interface OutcomeProjection {
    Timestamp getCreatedAt();
    Long getId();
    Double getAmount();
    String getCreatedBy();
    String getType();
    String getRoleName();
    String getUser();
}
