package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import demobreadshop.domain.base.BaseEntity;
import demobreadshop.domain.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class WareHouse extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    // comment for the product
    private String description;

    // type kg
    private double amount;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "material")
    private Set<ProductList> materials;
}
