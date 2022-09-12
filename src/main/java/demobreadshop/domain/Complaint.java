package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import demobreadshop.domain.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "complaint")
@NoArgsConstructor
@AllArgsConstructor
public class Complaint extends BaseEntity {
    private String description;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(optional = false)
    private User user;

    private Double amount;

    private String fileName;

    private String contentType;

    public Complaint(String description, User user, Double amount, String contentType) {
        this.description = description;
        this.user = user;
        this.amount = amount;
        this.contentType = contentType;
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> response = new HashMap<>();
        response.put("id", this.getId());
        response.put("fullName", this.getUser().getFullName());
        response.put("amount", this.getAmount());
        response.put("fileName", this.getFileName());
        response.put("description", this.getDescription());
        response.put("createdAt", this.getCreatedAt());
        return response;
    }
}
