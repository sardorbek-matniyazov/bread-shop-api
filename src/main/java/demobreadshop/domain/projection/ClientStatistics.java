package demobreadshop.domain.projection;

public interface ClientStatistics {
    String getFullName();

    boolean isKindergarten();

    String getPhoneNumber();

    Long getClientId();

    Double getWholePrice();
}
