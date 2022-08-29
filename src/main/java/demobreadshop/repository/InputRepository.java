package demobreadshop.repository;

import demobreadshop.domain.Input;
import demobreadshop.domain.enums.ProductType;
import demobreadshop.domain.enums.RoleName;
import demobreadshop.domain.projection.GroupStatistics;
import demobreadshop.domain.projection.InputStatistics;
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

    @Query(
            value = "select wh.id as productId, sum(i.amount) as amount, wh.name as name, wh.price as price, wh.updated_at as updatedAt, sum(i.amount * wh.price) as wholePrice\n" +
                    "from input i join ware_house wh on wh.id = i.material_id\n" +
                    "where i.type = ?1 \n" +
                    "group by wh.id;",
            nativeQuery = true
    )
    List<InputStatistics> getAllInputStatistics(String type);
}
