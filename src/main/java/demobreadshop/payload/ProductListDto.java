package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListDto {
    @NotNull(message = "material id shouldn't be null")
    private long materialId;

    @Min(value = 1)
    @NotNull(message = "material amount shouldn't be null")
    private double amount;

    private String description;
}
