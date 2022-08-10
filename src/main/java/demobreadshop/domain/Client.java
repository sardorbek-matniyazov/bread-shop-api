package demobreadshop.domain;

import demobreadshop.domain.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "client")
public class Client extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    private String comment;
}
