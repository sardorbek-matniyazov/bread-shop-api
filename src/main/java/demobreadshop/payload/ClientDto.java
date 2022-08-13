package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    @Size(min = 4, max = 50, message = "Client's name should be in [4, 50]")
    @NotBlank(message = "Client's name shouldn't be null")
    private String name;

    @Size(min = 7, max = 50, message = "Client's phone number should be in [7, 50]")
    @NotBlank(message = "Client's phone number shouldn't be null")
    private String phoneNumber;

    private String comment;

}
