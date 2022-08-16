package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {

    @NotNull(message = "Client's id shouldn't be null")
    private long clientId;
    @NotNull(message = "Product's id shouldn't be null")
    private long productId;
    @NotNull(message = "Amount shouldn't be null")
    private double amount;

    private double costCash;

    private double costCard;
}
