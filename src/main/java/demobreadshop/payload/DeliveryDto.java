package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    @NotNull(message = "Delivery id should not be null")
    private long deliveryId;

    @NotNull(message = "Product id should not be null")
    private long productId;

    @NotNull(message = "Delivery amount should not be null")
    private double amount;
}
