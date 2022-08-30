package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Role extends BaseEntity implements GrantedAuthority {
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
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

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> response = new HashMap<>();
        response.put("id", this.getId());
        response.put("name", this.getRoleName());
        return response;
    }
}
