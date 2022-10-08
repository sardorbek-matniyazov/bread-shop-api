package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import demobreadshop.domain.base.BaseEntity;
import demobreadshop.domain.enums.PayType;
import demobreadshop.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "pay_archive")
@NoArgsConstructor
@AllArgsConstructor
public class PayArchive extends BaseEntity {
    @Column(name = "archive_amount")
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_type")
    private PayType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_status")
    private PaymentStatus status;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    private Sale sale;

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> response = new HashMap<>();
        response.put("id", this.getId());
        response.put("type", this.getType());
        response.put("amount", this.getAmount());
        response.put("status", this.getStatus());
        response.put("createdAt", this.getCreatedAt());
        return response;
    }
}
