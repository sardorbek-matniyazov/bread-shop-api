package demobreadshop.domain;

import demobreadshop.domain.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Delivery extends BaseEntity {

    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne
    private User deliverer;

    private double pocket;

    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne
    private Output output;
}
