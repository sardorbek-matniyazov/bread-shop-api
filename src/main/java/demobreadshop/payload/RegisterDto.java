package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotBlank(message = "fullName shouldn't be null")
    private String fullName;

    @Size(min = 4, max = 50)
    @NotBlank(message = "phone number shouldn't be null, and size should be in [5, 50]")
    private String phoneNumber;

    @Size(min = 4, max = 20)
    @NotBlank(message = "role shouldn't be null, and size should be in [5, 20]")
    private String password;

    @NotNull(message = "role shouldn't be null")
    private long roleId;
}
