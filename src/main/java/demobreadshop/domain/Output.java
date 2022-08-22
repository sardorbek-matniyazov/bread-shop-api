package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import demobreadshop.domain.base.BaseInput;
import demobreadshop.domain.enums.OutputType;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
