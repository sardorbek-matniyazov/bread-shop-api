package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputDto {
    @NotNull(message = "product id shouldn't be null")
    private long productId;
    @NotNull(message = "product amount shouldn't be null")
    private double amount;
}
