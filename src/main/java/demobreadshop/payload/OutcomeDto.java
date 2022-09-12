package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutcomeDto {
    @NotBlank(message = "The outcome type shouldn't be null")
    private String type;
    @NotNull(message = "Money amount shouldn't be null")
    private double moneyAmount;
    private long userId;
    @NotBlank(message = "Comment shouldn't be null")
    private String comment;
}
