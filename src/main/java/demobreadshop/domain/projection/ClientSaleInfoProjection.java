package demobreadshop.domain.projection;

public interface ClientSaleInfoProjection {
    Double getWholePrice();
    Double getPaidPrice();
    Double getAmount();
    String getProduct();
}
