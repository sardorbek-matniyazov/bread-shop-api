package demobreadshop.domain;

import demobreadshop.domain.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "client")
@NoArgsConstructor
@AllArgsConstructor
public class Client extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    private String comment;

    @OneToMany(mappedBy = "client", cascade = CascadeType.MERGE)
    private Set<Sale> sales;

    public Client(String fullName, String phoneNumber, String comment) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.comment = comment;
    }

    public Client(long id, String fullName, String phoneNumber, String comment) {
        super(id);
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.comment = comment;
    }
}
