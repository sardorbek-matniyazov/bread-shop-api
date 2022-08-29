package demobreadshop.domain.projection;

public interface SellerStatistics {
    Double getSumAmount();
    String getFullName();
    String getPhoneNumber();
    Long userId();
}
