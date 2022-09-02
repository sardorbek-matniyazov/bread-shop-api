package demobreadshop.domain;

import demobreadshop.domain.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "complaint")
@NoArgsConstructor
@AllArgsConstructor
public class Complaint extends BaseEntity {
    private String description;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(optional = false)
    private User user;

    private Double amount;

    private String fileName;

    public Complaint(String description, User user, Double amount) {
        this.description = description;
        this.user = user;
        this.amount = amount;
    }
}
