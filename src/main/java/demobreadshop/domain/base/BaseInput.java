package demobreadshop.domain.base;

import demobreadshop.domain.WareHouse;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Setter
@Getter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseInput extends BaseEntity {
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne
    private WareHouse material;

    private double amount;
}
