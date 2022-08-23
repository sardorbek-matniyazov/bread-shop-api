package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import demobreadshop.domain.base.BaseInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

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

    public ProductList(WareHouse material, double amount) {
        super(material, amount);
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> response = new HashMap<>();
        response.put("product", this.getMaterial().getName());
        response.put("amount", this.getAmount());
        return response;
    }
}
