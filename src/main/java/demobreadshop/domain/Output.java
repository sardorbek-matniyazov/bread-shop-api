package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import demobreadshop.domain.base.BaseInput;
import demobreadshop.domain.enums.OutputType;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Output extends BaseInput {

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private OutputType status;

    public Output(WareHouse material, double amount, OutputType status) {
        super(material, amount);
        this.status = status;
    }

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Delivery delivery;

    public Output(WareHouse material, double amount, OutputType status, Delivery delivery) {
        super(material, amount);
        this.status = status;
        this.delivery = delivery;
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> response = new HashMap<>();
        response.put("id", this.getId());
        response.put("product", this.getMaterial().getName());
        response.put("amount", this.getAmount());
        response.put("createdBy", this.getCreatedBy());
        response.put("createdAt", this.getCreatedAt());
        return response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Output output = (Output) o;
        return getId() != null && Objects.equals(getId(), output.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
