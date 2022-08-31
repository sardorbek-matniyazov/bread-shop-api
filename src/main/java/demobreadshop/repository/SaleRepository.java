package demobreadshop.repository;

import demobreadshop.domain.Sale;
import demobreadshop.domain.enums.Status;
import demobreadshop.domain.projection.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findAllByType(Status debt);

    // uliwmaliq sawda
    @Query(
            value = "SELECT SUM(whole_price) FROM sale where created_at >= ?1 and created_at <= ?2",
            nativeQuery = true
    )
    Double sumOfIncome(Timestamp time, Timestamp timestamp);

    // uliwmaliq qariz summalar
    @Query(
            value = "SELECT SUM(debt_price) FROM sale",
            nativeQuery = true
    )
    Double sumOfDebt();

    // sellerler satiwina qaray qancha summa tapqani dastavchik ham admin seller ler
    @Query(
            value = "with bum as (SELECT count(*) as val, sum(o.material_amount) as amount, sum(o.material_amount * u.user_kpi) as allSum, u.full_name, u.user_kpi, u.users_id as userId, u.roles_id as roleId\n" +
                    "FROM (sale s join (users k JOIN users_roles r ON k.id = r.users_id) u on s.created_by = u.full_name) u inner join output o on u.full_name = o.created_by\n" +
                    "where o.output_type='O_SALE' and u.roles_id = ?1 and o.created_at >= ?2 and o.created_at <= ?3\n" +
                    "group by u.full_name, u.user_kpi, u.users_id, u.roles_id, o.output_type)\n" +
                    "select bum.amount / sqrt(bum.val) as amount, bum.allSum / sqrt(bum.val) as allSum, bum.user_kpi as userKpi, bum.full_name as fullName, bum.userId as userId from bum;\n",
            nativeQuery = true
    )
    List<SaleStatistics> getAllUserInfoByRoleId(Long id, Timestamp time, Timestamp timestamp);

    // isletilip atrg'an ingredientler statisticasi l
    @Query(
            value = "with bum as (select sum(input.material_amount * pl.material_amount) as sum, pl.material_id as material_id\n" +
                    "from input join product_list pl on input.material_id = pl.warehouse_fk\n" +
                    "where input.created_at >= ?1 and input.created_at <= ?2\n" +
                    "group by pl.material_id)\n" +
                    "select bum.sum as sum, w.wh_name as name, w.wh_price as price, bum.sum * w.wh_price as wholePrice, w.id\n" +
                    "from bum inner join ware_house w on bum.material_id = w.id",
            nativeQuery = true
    )
    List<MaterialDecreaseStat> getAllMaterialDecrease(Timestamp start, Timestamp end);

    // seller lar jiynap kelgen aqcha
    @Query(
            value = "with bum as\n" +
                    "(select sum(pa.archive_amount) as sum, s.id, s.created_by, s.created_at\n" +
                    " from sale s join pay_archive pa on s.id = pa.sale_id\n" +
                    " where s.created_at >= ?1 and s.created_at <= ?2\n" +
                    " group by s.id, s.created_by)\n" +
                    "select sum(bum.sum) as sumAmount, bum.created_by as fullName, u.phone_number, u.users_id as userId\n" +
                    "from bum join (users u join users_roles ur on u.id = ur.users_id) u on bum.created_by = u.full_name\n" +
                    "group by u.phone_number, bum.created_by, u.users_id;",
            nativeQuery = true
    )
    List<SellerStatistics> getAllUserStatistics(Timestamp time, Timestamp timestamp);

    // clientler statisticasi grafic kim ko'p sawda ishladi
    @Query(
            value = "select c.full_name as fullName, c.phone_number as phoneNumber, c.id as clientId, sum(s.whole_price) as wholePrice\n" +
                    "from sale s join client c on c.id = s.client_id\n" +
                    "where s.created_at >= ?1 and s.created_at <= ?2\n" +
                    "group by c.created_by, c.id;",
            nativeQuery = true
    )
    List<ClientStatistics> getAllClientSale(Timestamp start, Timestamp end);

    // kpi bo'yincha qo'shilg'an summa istoriyasi
    @Query(
            value = "select sum(o.material_amount * u.user_kpi) as amount, o.created_at as createdAt\n" +
                    "from output o join users u on o.created_by = u.full_name\n" +
                    "where output_type = 'O_SALE' and u.id = ?1 and o.created_at >= ?2 and o.created_at <= ?3\n" +
                    "group by o.created_at;",
            nativeQuery = true
    )
    List<SalaryHistoryProjection> findAllSalaryHistory(Long id, Timestamp time, Timestamp timestamp);

    @Query(
            value = "with bum as (\n" +
                    "select sum(pa.archive_amount) as amount, s.id, pa.pay_type\n" +
                    "from sale s join pay_archive pa on s.id = pa.sale_id\n" +
                    "group by pa.pay_type, s.id\n" +
                    ") select sum(bum.amount) as amount, bum.pay_type as payType\n" +
                    "from sale s join bum on bum.id = s.id\n" +
                    "where s.created_at >= ?1 and s.created_at <= ?2\n" +
                    "group by bum.pay_type;",
            nativeQuery = true
    )
    List<SaleInfoProjection> getSaleInfo(Timestamp time, Timestamp timestamp);

    // clientlarding uliwma bergan summalari
    @Query(
            value = "with bum as (\n" +
                    "select sum(pa.archive_amount) as amount, s.id, pa.pay_type, client_id\n" +
                    "from sale s join pay_archive pa on s.id = pa.sale_id\n" +
                    "group by pa.pay_type, s.id\n" +
                    ") select sum(bum.amount) as amount, c.full_name\n" +
                    "from client c join bum on bum.client_id = c.id\n" +
                    "group by c.full_name;",
            nativeQuery = true
    )
    List<ClientSumStatistics> getAllClientPayedSums();

    // qarizdarlar sani
    @Query(
            value = "select count(c.id), c.full_name\n" +
                    "from sale s join client c on s.client_id = c.id\n" +
                    "where s.status = 'DEBT'\n" +
                    "group by c.full_name;",
            nativeQuery = true
    )
    List<?> countOfDebtClients();

    @Query(
            value = "select c.full_name as fullName, sum(s.whole_price) as amount\n" +
                    "from sale s join client c on c.id = s.client_id\n" +
                    "where s.created_at >= ?1 and s.created_at <= ?2\n" +
                    "group by c.created_by, c.id;",
            nativeQuery = true
    )
    List<AllClientIncomeProjection> allClientIncome(Timestamp start, Timestamp end);

    @Query(
            value = "SELECT SUM(debt_price) FROM sale where created_at >= ?1 and created_at <= ?2",
            nativeQuery = true
    )
    Double sumOfDebtByTime(Timestamp time, Timestamp time1);

    @Query(
            value = "select * from sale\n" +
                    "where created_by = ?1 and created_at >= ?2 and created_at <= ?3",
            nativeQuery = true
    )
    List<Sale> getAllSellerSales(String fullName, Timestamp start, Timestamp end);
}
