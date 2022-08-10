package demobreadshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResult {
    private String timestamp;
    private int status = 400;
    private String message;

    public ErrorResult(String message) {
        this.message = message;
        this.timestamp = Timestamp.valueOf(LocalDateTime.now()).toString();
    }
}
