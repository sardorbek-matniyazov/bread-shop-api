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
    @NotBlank(message = "field fullName shouldn't be null")
    private String fullName;

    @Size(min = 4, max = 50, message = "Phone number should be in [4, 50]")
    @NotBlank(message = "phone number shouldn't be null")
    private String phoneNumber;

    @Size(min = 4, max = 50, message = "Password should be in [4, 50]")
    @NotBlank(message = "Role shouldn't be null")
    private String password;

    @NotNull(message = "Role shouldn't be null")
    private long roleId;
}
