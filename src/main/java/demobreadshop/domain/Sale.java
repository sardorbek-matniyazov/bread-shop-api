package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import demobreadshop.domain.base.BaseEntity;
import demobreadshop.domain.enums.SaleType;
import demobreadshop.domain.enums.SaleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Sale extends BaseEntity {
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Output output;

    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne
    private Client client;

    @Column(name = "whole_price")
    private double wholePrice;

    @Column(name = "debt_price")
    private double debtPrice;

    @Column(name = "product_price")
    private double productPrice;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SaleStatus type;

    @Column(name = "user_kpi_value")
    private Double kpiValue;

    public Sale(
            Output output,
            Client client,
            double wholePrice,
            double debtPrice,
            double productPrice,
            SaleStatus type,
            Double userKpi) {
        this.output = output;
        this.client = client;
        this.productPrice = productPrice;
        this.wholePrice = wholePrice;
        this.debtPrice = debtPrice;
        this.type = type;
        this.kpiValue = userKpi;
    }

    public Sale(Long id, Double debtPrice) {
        super(id);
        this.debtPrice = debtPrice;
    }
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "sale_type")
    private SaleType saleType;

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> response = new HashMap<>();
        response.put("id", this.getId());
        response.put("amount", this.getOutput().getAmount());
        response.put("product", this.getOutput().getMaterial().getName());
        response.put("client", this.getClient().getFullName());
        response.put("wholePrice", BigDecimal.valueOf(this.getWholePrice()));
        response.put("productPrice", BigDecimal.valueOf(this.getProductPrice()));
        response.put("paidPrice", BigDecimal.valueOf(this.getWholePrice() - this.getDebtPrice()));
        response.put("debtPrice", BigDecimal.valueOf(this.getDebtPrice()));
        response.put("type", this.getType());
        response.put("createdBy", this.getCreatedBy());
        response.put("createdAt", this.getCreatedAt());
        return response;
    }
}
