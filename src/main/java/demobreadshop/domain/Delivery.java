package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import demobreadshop.domain.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Delivery extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;
    private String description;

    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne
    private User deliverer;

    private double pocket;

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Output> outputs;
}
