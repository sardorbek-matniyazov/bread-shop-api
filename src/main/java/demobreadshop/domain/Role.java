package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import demobreadshop.domain.base.BaseEntity;
import demobreadshop.domain.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Role extends BaseEntity implements GrantedAuthority {
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    public Role(long id, Timestamp createdDate, Timestamp updatedDate, String createdBy) {
        super(id, createdDate, updatedDate, createdBy);
    }

    public Role(RoleName roleName) {
        this.roleName = roleName;
    }

    public Role() {
        super();
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
        return roleName.name();
    }
}
