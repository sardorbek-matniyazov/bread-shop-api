package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import demobreadshop.domain.base.BaseEntity;
import demobreadshop.domain.enums.SaleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Sale extends BaseEntity {
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(cascade = CascadeType.MERGE)
    private Output output;

    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<PayArchive> archives;

    private double wholePrice;

    private double debtPrice;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private SaleType type;

    @JsonValue
    public Map<String, Object> toJson(){
        Map<String, Object> response = new HashMap<>();
        response.put("id", this.getId());
        response.put("amount", getOutput().getAmount());
        response.put("product", getOutput().getMaterial().getName());
        response.put("client", getClient().getFullName());
        response.put("wholePrice", getWholePrice());
        response.put("debtPrice", getDebtPrice());
        response.put("type", getType());
        response.put("createdBy", getCreatedBy());
        response.put("createdAt", getCreatedAt());
        return response;
    }
}
