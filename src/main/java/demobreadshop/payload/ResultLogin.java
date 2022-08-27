package demobreadshop.payload;

import demobreadshop.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultLogin {
    private String message;
    private boolean active;
    private String token;
    private Optional<Role> role;
}
