package demobreadshop.repository;

import demobreadshop.domain.Input;
import demobreadshop.domain.enums.ProductType;
import demobreadshop.domain.enums.RoleName;
import demobreadshop.domain.projection.GroupStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InputRepository extends JpaRepository<Input, Long> {
    List<Input> findAllByType(ProductType type);

    @Query(
            value = "select sum(i.amount) as amount, u.full_name as name, u.userkpi as kpi, sum(i.amount * u.userkpi) as sum, u.users_id as userId\n" +
                    "from input i join (users u join users_roles ur on u.id = ur.users_id) u on i.created_by = u.full_name\n" +
                    "where u.roles_id = ?1 and i.type = 'PRODUCT'\n" +
                    "group by u.full_name, u.userkpi, u.users_id",
            nativeQuery = true
    )
    List<GroupStatistics> getAllGroupStatistics(Long roleId);
}
