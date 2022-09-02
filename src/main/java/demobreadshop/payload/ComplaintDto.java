package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintDto {
    @NotBlank(message = "Description shouldn't be null")
    private String description;
    @NotNull(message = "Amount shouldn't be null")
    private Double amount;
    @NotNull(message = "User shouldn't be null")
    private Long userId;
}
