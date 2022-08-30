package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import demobreadshop.domain.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends BaseEntity implements UserDetails {

    @Column(name = "full_name", nullable = false, unique = true)
    private String fullName;

    @Size(min = 4, max = 50, message = "Phone number should be in [4, 50]")
    @NotBlank(message = "phone number shouldn't be null")
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull(message = "User Kpi shouldn't be null")
    @Column(name = "user_kpi")
    private double userKPI;

    @Column(name = "balance")
    private double balance;

    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String fullName, String phoneNumber, String password, Set<Role> roles, double kpi) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.roles = roles;
        this.userKPI = kpi;
    }

    @JsonIgnore
    @Override
    public Set<Role> getAuthorities() {
        return this.roles;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
