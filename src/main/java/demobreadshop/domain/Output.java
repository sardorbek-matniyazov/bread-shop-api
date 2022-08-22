package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import demobreadshop.domain.base.BaseInput;
import demobreadshop.domain.enums.OutputType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
public class Output extends BaseInput {

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private OutputType status;

    public Output(WareHouse material, double amount, OutputType status) {
        super(material, amount);
        this.status = status;
    }

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToMany(mappedBy = "outputs", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Delivery> deliveries;

    public Output() {
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
