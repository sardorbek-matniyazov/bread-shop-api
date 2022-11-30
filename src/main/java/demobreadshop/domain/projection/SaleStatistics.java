package demobreadshop.domain.projection;

public interface SaleStatistics {
    Double getAmount();
    Double getAllSum();
    default String getUserKpi() {
        return "changed";
    }
    String getFullName();
    Long getUserId();
}
