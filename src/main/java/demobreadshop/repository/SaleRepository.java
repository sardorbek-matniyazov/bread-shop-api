package demobreadshop.repository;

import demobreadshop.domain.Sale;
import demobreadshop.domain.enums.Status;
import demobreadshop.domain.projection.MaterialDecreaseStat;
import demobreadshop.domain.projection.SaleStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findAllByType(Status debt);

    @Query(
            value = "SELECT SUM(whole_price) FROM sale",
            nativeQuery = true
    )
    Double sumOfIncome();

    @Query(
            value = "SELECT SUM(debt_price) FROM sale",
            nativeQuery = true
    )
    Double sumOfDebt();

    @Query(
            value = "with bum as (SELECT DISTINCT count(*) as val, sum(o.amount) as amount, sum(o.amount * u.userkpi) as allSum, u.full_name, u.userkpi, u.users_id as userId, u.roles_id as roleId\n" +
                    "FROM (sale s join (users k JOIN users_roles r ON k.id = r.users_id) u on s.created_by = u.full_name) u inner join output o on u.full_name = o.created_by\n" +
                    "where o.type='O_SALE' and u.roles_id = ?1\n" +
                    "group by u.full_name, u.userkpi, u.users_id, u.roles_id, o.type)\n" +
                    "select bum.amount / sqrt(bum.val) as amount, bum.allSum / sqrt(bum.val) as allSum, bum.userkpi, bum.full_name as fullName, bum.userId as userId from bum;",
            nativeQuery = true
    )
    List<SaleStatistics> getAllUserInfoByRoleId(Long id);

    @Query(
            value = "with bum as (select sum(input.amount * pl.amount) as sum, pl.material_id as material_id\n" +
                    "from input join product_list pl on input.material_id = pl.warehouse_fk\n" +
                    "group by pl.material_id)\n" +
                    "select bum.sum as amount, w.name as name, w.price as price, bum.sum * w.price as wholePrice, w.id from bum inner join ware_house w on bum.material_id = w.id",
            nativeQuery = true
    )
    List<MaterialDecreaseStat> getAllMaterialDecrease();
}
