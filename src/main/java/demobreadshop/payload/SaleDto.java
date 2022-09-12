package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {

    @NotNull(message = "Client kiritilishi kerak")
    private long clientId;
    @NotNull(message = "Maxsulot belgilanishi kerak")
    private long productId;
    @NotNull(message = "Maxsulot miqdori kiritilishi kerak")
    private double amount;

    private double costCash;

    private double costCard;
}
