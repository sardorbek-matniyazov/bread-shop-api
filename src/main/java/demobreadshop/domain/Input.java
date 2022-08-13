package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import demobreadshop.domain.base.BaseInput;
import demobreadshop.domain.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Input extends BaseInput {
    @JsonIgnore
    private ProductType type;

    public Input(WareHouse material, double amount, ProductType type) {
        super(material, amount);
        this.type = type;
    }
}
