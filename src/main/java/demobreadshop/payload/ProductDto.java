package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @NotBlank(message = "Product name shouldn't be null")
    private String name;

    @Min(value = 1)
    @NotNull(message = "Product price shouldn't be null")
    private double price;

    @NotNull(message = "Product type shouldn't be null")
    private String productType;

    List<ProductListDto> materials;

    private String description;
}
