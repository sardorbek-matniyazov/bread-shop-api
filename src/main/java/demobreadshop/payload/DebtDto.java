package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtDto {
    @NotNull(message = "Sale id shouldn't be null")
    private long saleId;
    private double costCash;
    private double costCard;
}
