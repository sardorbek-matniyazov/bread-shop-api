package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import demobreadshop.domain.base.BaseInput;
import demobreadshop.domain.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Input extends BaseInput {
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "input_type")
    private ProductType type;

    @Column(name = "product_price")
    private double productPrice;

    @Column(name = "user_kpi_value")
    private double kpiValue;

    public Input(WareHouse material, double amount, ProductType type, double kpiValue,  double productPrice) {
        super(material, amount);
        this.type = type;
        this.productPrice = productPrice;
        this.kpiValue = kpiValue;
    }

    public Input(WareHouse material, double amount, ProductType type, Double kpiValue) {
        super(material, amount);
        this.type = type;
        this.kpiValue = kpiValue;
    }

    public Input(WareHouse material, double amount, ProductType type) {
        super(material, amount);
        this.type = type;
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> response = new HashMap<>();
        response.put("id", this.getId());
        response.put("product", getMaterial().getName());
        response.put("amount", getAmount());
        response.put("createdBy", getCreatedBy());
        response.put("createdAt", getCreatedAt());
        return response;
    }
}
