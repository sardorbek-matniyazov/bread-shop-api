package demobreadshop.domain;

import demobreadshop.domain.base.BaseEntity;
import demobreadshop.domain.enums.PayType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class PayArchive extends BaseEntity {
    private double amount;

    @Enumerated(EnumType.STRING)
    private PayType type;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    private Sale sale;
}
