package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @Size(min = 4, max = 50, message = "User's phone number should be in [4, 50]")
    @NotBlank(message = "User's Phone number shouldn't be null")
    private String phoneNumber;

    @Size(min = 4, max = 50, message = "User's password should be in [4, 50]")
    @NotBlank(message = "Password shouldn't be null")
    private String password;
}
