package demobreadshop.repository;

import demobreadshop.domain.Input;
import demobreadshop.domain.enums.InputType;
import demobreadshop.domain.enums.ProductType;
import demobreadshop.domain.projection.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface InputRepository extends JpaRepository<Input, Long> {
    List<Input> findAllByTypeOrderByIdDesc(ProductType type);

    @Query(
            value = "select i.id as id, wr.wh_name as product, i.material_amount as amount, i.created_by as createdBy, i.created_at as createdAt " +
                    "    from ware_house wr, input i, users u, users_roles ur, role r " +
                    "    where i.material_id = wr.id and i.created_by = u.full_name and u.id = ur.users_id and ur.roles_id = r.id and r.role_name = ?1 and wr.product_type = ?2 " +
                    "    order by i.id DESC;",
            nativeQuery = true
    )
    List<InputDataProjection> findAllInputByRoleAndProduct_Type(String roleName, String type);
    
    @Query(
            value = "select sum(i.material_amount) as amount, u.full_name as name, u.user_kpi as kpi, sum(i.material_amount * i.user_kpi_value) as sum, u.users_id as userId " +
                    "from input i join (users u join users_roles ur on u.id = ur.users_id) u on i.created_by = u.full_name " +
                    "where u.roles_id = ?1 and i.input_type = 'PRODUCT' and i.created_at >= ?2 and i.created_at <= ?3 " +
                    "group by u.full_name, u.user_kpi, u.users_id",
            nativeQuery = true
    )
    List<GroupStatistics> getAllGroupStatistics(Long roleId, Timestamp time, Timestamp timestamp);

    @Query(
            value = "select wh.id as productId, sum(i.material_amount) as amount, wh.wh_name as name, wh.updated_at as updatedAt, sum(i.material_amount * i.product_price) as sum " +
                    "from input i join ware_house wh on wh.id = i.material_id " +
                    "where wh.product_type = ?1 and i.created_at >= ?2 and i.created_at <= ?3 " +
                    "group by wh.id",
            nativeQuery = true
    )
    List<InputStatistics> getAllInputStatistics(String type, Timestamp time, Timestamp timestamp);

    @Query(
            value = "select i.material_amount as amount, i.user_kpi_value as userKpi, (i.material_amount * i.user_kpi_value) as allSum,  i.created_by as fullName, i.created_at as createdAt " +
                    "from input i " +
                    "where i.created_by = ?1 and i.created_at >= ?2 and i.created_at <= ?3 order by i.id DESC ",
            nativeQuery = true
    )
    List<SalaryHistoryProjection> getAllInputSalaryHistory(String fullName, Timestamp time, Timestamp timestamp);

    @Query(
            value = "select sum(i.material_amount * i.product_price) as price, sum(i.material_amount) as amount " +
                    "from input i where i.created_at >= ?1 and i.created_at <= ?2 and i.input_type = 'PRODUCT';",
            nativeQuery = true
    )
    InputProjection findAmountOfProduct(Timestamp timestamp, Timestamp timestamp1);

    @Query(
            value = "select sum(i.material_amount * i.real_price) as price, sum(i.material_amount) as amount " +
                    "from input i where i.created_at >= ?1 and i.created_at <= ?2 and i.input_type = 'PRODUCT'",
            nativeQuery = true
    )
    InputProjection findAmountOfWarehouse(Timestamp timestamp, Timestamp timestamp1);

    List<Input> findByCreatedByAndInputType(String fullName, InputType inputType);
}
