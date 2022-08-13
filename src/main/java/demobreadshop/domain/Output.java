package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import demobreadshop.domain.base.BaseInput;
import demobreadshop.domain.enums.OutputType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
public class Output extends BaseInput {

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private OutputType status;

    @JsonIgnore
    @OneToMany(mappedBy = "output", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<Sale> sales;

    @JsonIgnore
    @OneToMany(mappedBy = "output", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<Delivery> deliveries;

    public Output() {
    }
}
