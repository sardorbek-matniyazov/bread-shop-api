package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerAccessDto {
    @NotNull(message = "adminOne Id is required")
    private Long adminOneId;

    @NotNull(message = "adminTwo Id is required")
    private Long adminTwoId;
}
