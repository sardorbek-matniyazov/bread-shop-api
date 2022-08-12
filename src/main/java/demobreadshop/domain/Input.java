package demobreadshop.domain;

import demobreadshop.domain.base.BaseInput;
import demobreadshop.domain.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Input extends BaseInput {
    private ProductType type;

    public Input(WareHouse material, double amount, ProductType type) {
        super(material, amount);
        this.type = type;
    }
}
