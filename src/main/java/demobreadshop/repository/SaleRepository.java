package demobreadshop.repository;

import demobreadshop.domain.Sale;
import demobreadshop.domain.enums.SaleStatus;
import demobreadshop.domain.projection.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findAllByTypeAndClient_IsKindergarten(SaleStatus debt, boolean isKindergarten, Sort sort);

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
            value = "with bum as (select sum(s.user_kpi_value * o.material_amount) as allSum, " +
                    "       s.created_by as fullName, " +
                    "       sum(o.material_amount) as amount " +
                    "from sale s join output o on s.output_id = o.id " +
                    "where s.created_at >= ?2 and s.created_at <= ?3 " +
                    "group by s.created_by) " +
                    "SELECT bum.allSum, bum.fullName, bum.amount, users_id as userId " +
                    "from bum, users, users_roles " +
                    "where bum.fullName = users.full_name and users.id = users_roles.users_id and users_roles.roles_id = ?1",
            nativeQuery = true
    )
    List<SaleStatistics> getAllUserInfoByRoleId(Long id, Timestamp time, Timestamp timestamp);

    // isletilip atrg'an ingredientler statisticasi
    @Query(
            value = "with bum as (select sum(input.material_amount * pl.material_amount) as sum, pl.material_id as material_id " +
                    "from input join product_list pl on input.material_id = pl.warehouse_fk " +
                    "where input.created_at >= ?1 and input.created_at <= ?2 " +
                    "group by pl.material_id) " +
                    "select bum.sum as amount, w.wh_name as name, w.wh_price as price, bum.sum * w.wh_price as wholePrice, w.id " +
                    "from bum inner join ware_house w on bum.material_id = w.id",
            nativeQuery = true
    )
    List<MaterialDecreaseStat> getAllMaterialDecrease(Timestamp start, Timestamp end);

    // seller lar jiynap kelgen aqcha
    @Query(
            value = "select sum(pa.archive_amount) as sumAmount, u.full_name as fullName, u.phone_number as phoneNumber " +
                    "from pay_archive pa join users u on pa.created_by = u.full_name " +
                    "where pa.created_at >= ?1 and pa.created_at <= ?2 " +
                    "group by u.full_name, u.phone_number;",
            nativeQuery = true
    )
    List<SellerStatistics> getAllUserStatistics(Timestamp time, Timestamp timestamp);

    // clientler statisticasi grafic kim ko'p sawda ishladi
    @Query(
            value = "select c.full_name as fullName, c.is_kindergarten as kindergarten, c.phone_number as phoneNumber, c.id as clientId, sum(s.whole_price) as wholePrice " +
                    "from sale s join client c on c.id = s.client_id " +
                    "where s.created_at >= ?1 and s.created_at <= ?2 " +
                    "group by c.created_by, c.id;",
            nativeQuery = true
    )
    List<ClientStatistics> getAllClientSale(Timestamp start, Timestamp end);

    // kpi bo'yincha qo'shilg'an summa istoriyasi
    @Query(
            value = "select (o.material_amount * u.user_kpi) as allSum, o.created_at as createdAt, o.material_amount as amount, o.created_by as fullName, u.user_kpi as userKpi " +
                    "    from output o join users u on o.created_by = u.full_name " +
                    "    where output_type = 'O_SALE' and u.id = ?1 and o.created_at >= ?2 and o.created_at <= ?3 order by o.id desc ",
            nativeQuery = true
    )
    List<SalaryHistoryProjection> findAllSalaryHistory(Long id, Timestamp time, Timestamp timestamp);

    @Query(
            value = "with bum as (\n" +
                    "select sum(pa.archive_amount) as amount, s.id, pa.pay_type " +
                    "from sale s join pay_archive pa on s.id = pa.sale_id " +
                    "where pa.pay_status = 'PAID' " +
                    "group by pa.pay_type, s.id " +
                    ") select sum(bum.amount) as amount, bum.pay_type as payType " +
                    "from sale s join bum on bum.id = s.id " +
                    "where s.created_at >= ?1 and s.created_at <= ?2 " +
                    "group by bum.pay_type;",
            nativeQuery = true
    )
    List<SaleInfoProjection> getSaleInfo(Timestamp time, Timestamp timestamp);

    // clientlarding uliwma bergan summalari
    @Query(
            value = "with bum as (\n" +
                    "    select sum(pa.archive_amount) as amount, s.id, pa.pay_type, client_id " +
                    "    from sale s join pay_archive pa on s.id = pa.sale_id " +
                    "    where s.created_at >= ?1 and s.created_at <= ?2 and pa.pay_status = 'PAID' " +
                    "    group by pa.pay_type, s.id " +
                    "    ) select sum(bum.amount) as amount, c.full_name as fullName " +
                    "    from client c join bum on bum.client_id = c.id " +
                    "    group by c.full_name;",
            nativeQuery = true
    )
    List<ClientSumStatistics> getAllClientPayedSums(Timestamp timestamp, Timestamp timestamp1);

    // qarizdarlar sani ..
    @Query(
            value = "select count(c.id), c.full_name " +
                    "from sale s join client c on s.client_id = c.id " +
                    "where s.status = 'DEBT' " +
                    "group by c.full_name;",
            nativeQuery = true
    )
    List<?> countOfDebtClients();

    @Query(
            value = "select c.full_name as fullName, sum(s.whole_price) as amount " +
                    "from sale s join client c on c.id = s.client_id " +
                    "where s.created_at >= ?1 and s.created_at <= ?2 " +
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
            value = "select * from sale " +
                    "where created_by = ?1 and created_at >= ?2 and created_at <= ?3",
            nativeQuery = true
    )
    List<Sale> getAllSellerSales(String fullName, Timestamp start, Timestamp end);

    @Query(
            value = "SELECT * from sale where client_id = ?1 and created_at >= ?2 and created_at <= ?3",
            nativeQuery = true
    )
    List<Sale> getAllClientSaleInfo(Long clientId, Timestamp timestamp, Timestamp timestamp1);

    @Query(
            value = "select sum(pa.archive_amount) as amount, pa.pay_type as payType " +
                    "from sale s join pay_archive pa on s.id = pa.sale_id " +
                    "where pa.pay_type = ?1 and s.created_by = ?2 and s.created_at >= ?3 and s.created_at <= ?4 " +
                    "group by pa.pay_type, s.created_by;",
            nativeQuery = true
    )
    SaleInfoProjection getSalePayInfo(String payType, String fullName, Timestamp timestamp, Timestamp timestamp1);

    @Query(
            value = "UPDATE sale " +
                    "SET debt_price = debt_price - ?1, " +
                    "status = case " +
                    "   when debt_price - ?1 = 0 then 'PAYED' " +
                    "   else 'DEBT' " +
                    "end " +
                    "where id = ?2",
            nativeQuery = true
    )
    @Modifying
    Integer setDebtPriceWithPayArchive(double amount, long saleId);

    @Query(
            value = "select sum(s.debt_price) from sale s join client c on c.id = s.client_id where c.is_kindergarten;",
            nativeQuery = true
    )
    Double sumOfKindergarten();

    @Query(
            value = "select sum(pay_archive.archive_amount) from pay_archive where sale_id = ?1 and pay_status='WAIT' ",
            nativeQuery = true
    )
    Double sumOfDebtByStatusWait(Long id);

    @Query(
            value = "select c.id as id, sum(s.debt_price) as debtAmount, c.full_name as client, c.is_kindergarten as kindergarten " +
                    "    from client c join sale s on c.id = s.client_id group by c.id, c.is_kindergarten;",
            nativeQuery = true
    )
    List<ClientsDebt> getAllClientDebt();

    List<Sale> findAllByTypeAndClient_IsKindergartenAndCreatedBy(SaleStatus type, boolean isKindergarten, String fullName, Sort id);

    // sellerlar qilg'an qarajatlar
    @Query(
            value = "select sum(outcome_amount) as sumAmount, u.full_name as fullName, u.phone_number as phoneNumber " +
                    "    from outcome o join (users join users_roles on users.id = users_roles.users_id) u on o.created_by = u.full_name " +
                    "    where o.created_at >= ?1 and o.created_at <= ?2 and (u.roles_id = 2 or u.roles_id = 3) " +
                    "    group by u.full_name, u.phone_number;",
            nativeQuery = true
    )
    List<SellerStatistics> getAllUserOutcomeStatistics(Timestamp time, Timestamp time1);
}
