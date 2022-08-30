package demobreadshop.domain.base;

import demobreadshop.domain.WareHouse;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Setter
@Getter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseInput extends BaseEntity {
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(cascade = CascadeType.MERGE)
    private WareHouse material;

    @Column(name = "material_amount")
    private double amount;
}
