package demobreadshop.domain;

import demobreadshop.domain.base.BaseInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProductList extends BaseInput {
    private String description;

    public ProductList(WareHouse material, double amount, String description) {
        super(material, amount);
        this.description = description;
    }
}
