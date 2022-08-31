package demobreadshop.repository;

import demobreadshop.domain.Input;
import demobreadshop.domain.enums.ProductType;
import demobreadshop.domain.enums.RoleName;
import demobreadshop.domain.projection.GroupStatistics;
import demobreadshop.domain.projection.InputStatistics;
import demobreadshop.domain.projection.SalaryHistoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InputRepository extends JpaRepository<Input, Long> {
    List<Input> findAllByType(ProductType type);

    @Query(
            value = "select sum(i.material_amount) as amount, u.full_name as name, u.user_kpi as kpi, sum(i.material_amount * u.user_kpi) as sum, u.users_id as userId\n" +
                    "from input i join (users u join users_roles ur on u.id = ur.users_id) u on i.created_by = u.full_name\n" +
                    "where u.roles_id = ?1 and i.input_type = 'PRODUCT'\n" +
                    "group by u.full_name, u.user_kpi, u.users",
            nativeQuery = true
    )
    List<GroupStatistics> getAllGroupStatistics(Long roleId);

    @Query(
            value = "select wh.id as productId, sum(i.material_amount) as amount, wh.wh_name as name, wh.updated_at as updatedAt, sum(i.material_amount * i.product_price) as sum\n" +
                    "from input i join ware_house wh on wh.id = i.material_id\n" +
                    "where wh.product_type = ?1\n" +
                    "group by wh.id;",
            nativeQuery = true
    )
    List<InputStatistics> getAllInputStatistics(String type);

    @Query(
            value = "select i.material_amount as amount, i.user_kpi_value, i.created_by\n" +
                    "from input i\n" +
                    "where i.created_by = ?1;",
            nativeQuery = true
    )
    List<SalaryHistoryProjection> getAllInputSalaryHistory(String fullName);
}
