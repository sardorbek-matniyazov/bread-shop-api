package demobreadshop.domain;

import demobreadshop.domain.base.BaseInput;

import javax.persistence.Entity;

@Entity
public class ProductList extends BaseInput {
    private String description;
}
