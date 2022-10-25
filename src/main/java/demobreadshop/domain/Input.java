package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import demobreadshop.domain.base.BaseInput;
import demobreadshop.domain.enums.InputType;
import demobreadshop.domain.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
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

    @Column(name = "real_price")
    private double realPrice;

    @Column(name = "user_kpi_value")
    private double kpiValue;

    @Column(name = "input_status")
    @Enumerated(EnumType.STRING)
    private InputType inputType = InputType.ACCEPTED;

    public Input(WareHouse material, double amount, ProductType type, double kpiValue, InputType inputType,  double productPrice, Double realPrice) {
        super(material, amount);
        this.type = type;
        this.productPrice = productPrice;
        this.kpiValue = kpiValue;
        this.inputType = inputType;
        this.realPrice = realPrice;
    }

    public Input(WareHouse material, double amount, ProductType type, InputType inputType) {
        super(material, amount);
        this.type = type;
        this.inputType = inputType;
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
