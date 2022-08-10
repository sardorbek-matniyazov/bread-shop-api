package demobreadshop.domain;

import demobreadshop.domain.base.BaseEntity;
import demobreadshop.domain.enums.SaleType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Sale extends BaseEntity {
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne
    private Output output;

    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne
    private Client client;

    private double wholePrice;

    private double debtPrice;

    @Enumerated(EnumType.STRING)
    private SaleType type;

    private Timestamp expiredDate;
}
