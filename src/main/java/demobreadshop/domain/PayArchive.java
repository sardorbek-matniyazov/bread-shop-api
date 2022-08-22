package demobreadshop.domain;

import demobreadshop.domain.base.BaseEntity;
import demobreadshop.domain.enums.PayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PayArchive extends BaseEntity {
    private double amount;

    @Enumerated(EnumType.STRING)
    private PayType type;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    private Sale sale;

    public PayArchive(double amount, PayType type) {
        this.amount = amount;
        this.type = type;
    }
}
