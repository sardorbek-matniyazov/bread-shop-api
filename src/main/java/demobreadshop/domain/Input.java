package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import demobreadshop.domain.base.BaseInput;
import demobreadshop.domain.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

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

    @JsonValue
    public Map<String, Object> toJson(){
        Map<String, Object> response = new HashMap<>();
        response.put("id", this.getId());
        response.put("product", getMaterial().getName());
        response.put("amount", getAmount());
        response.put("createdBy", getCreatedBy());
        response.put("createdAt", getCreatedAt());
        return response;
    }
}
