package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import demobreadshop.domain.base.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Delivery extends BaseEntity {
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @OneToOne()
    private User deliverer;

    public Delivery(User deliverer, Set<ProductList> balance) {
        this.deliverer = deliverer;
        this.balance = balance;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "delivery_fk", referencedColumnName = "id")
    @ToString.Exclude
    private Set<ProductList> balance;

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> response = new HashMap<>();
        response.put("id", this.getId());
        response.put("deliverer", this.getDeliverer().getFullName());
        response.put("createdBy", this.getCreatedBy());
        response.put("createdAt", this.getCreatedAt());
        return response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Delivery delivery = (Delivery) o;
        return getId() != null && Objects.equals(getId(), delivery.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
