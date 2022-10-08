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
    private Double price;

    @NotNull(message = "Product type is required")
    private String productType;

    private Double kindergartenPrice;

    List<ProductListDto> materials;
}
