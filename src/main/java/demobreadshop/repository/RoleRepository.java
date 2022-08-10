package demobreadshop.repository;

import demobreadshop.domain.Role;
import demobreadshop.domain.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByRoleName(RoleName roleName);

    Role getByRoleName(RoleName glAdmin);
}
