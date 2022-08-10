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
    @Size(min = 4, max = 50)
    @NotBlank(message = "phone number shouldn't be null, and size should be in [5, 50]")
    private String phoneNumber;

    @Size(min = 4, max = 20)
    @NotBlank(message = "password shouldn't be null, and size should be in [5, 20]")
    private String password;
}
