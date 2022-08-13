package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputDto {
    @NotNull(message = "Product id shouldn't be null")
    private long productId;
    @NotNull(message = "Product amount shouldn't be null")
    private double amount;
}
