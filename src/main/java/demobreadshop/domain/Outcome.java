package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import demobreadshop.domain.base.BaseEntity;
import demobreadshop.domain.enums.OutcomeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Outcome extends BaseEntity {
    @Column(nullable = false)
    private double amount;

    @ManyToOne()
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OutcomeType type;

    private String comment;

    public Outcome(double amount, OutcomeType type, String comment) {
        this.amount = amount;
        this.type = type;
        this.comment = comment;
    }

    @JsonValue
    public Map<String, Object> toJson(){
        Map<String, Object> response = new HashMap<>();
        response.put("id", this.getId());
        response.put("type", this.getType().name());
        response.put("user", this.getUser() == null ? "none" : getUser().getFullName());
        response.put("amount", this.getAmount());
        response.put("createdBy", this.getCreatedBy());
        response.put("createdAt", this.getCreatedAt());
        return response;
    }
}
