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
    @NotBlank(message = "User nomi bo'lishi kerak")
    private String fullName;

    @Size(min = 4, max = 50, message = "User raqami 5 harfdan ko'p bo'lishi kerak")
    @NotBlank(message = "User raqami bo'lishi kerak")
    private String phoneNumber;

    @Size(min = 4, max = 50, message = "User paroli 5 harfdan ko'p bo'lishi kerak")
    @NotBlank(message = "User paroli bo'lishi kerak")
    private String password;

    @NotNull(message = "User kpi summasi kiritilishi kerak")
    private double userKPI;

    @NotNull(message = "User roli kiritilishi kerak")
    private long roleId;
}
