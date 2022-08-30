package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import demobreadshop.domain.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "client")
@NoArgsConstructor
@AllArgsConstructor
public class Client extends BaseEntity {

    @Column(name = "full_name", nullable = false, unique = true)
    private String fullName;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "comment")
    private String comment;

    public Client(long id, String fullName, String phoneNumber, String comment) {
        super(id);
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.comment = comment;
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> response = new HashMap<>();
        response.put("id", this.getId());
        response.put("fullName", getFullName());
        response.put("phoneNumber", getPhoneNumber());
        response.put("comment", getComment());
        response.put("createdBy", getCreatedBy());
        response.put("createdAt", getCreatedAt());
        return response;
    }
}
