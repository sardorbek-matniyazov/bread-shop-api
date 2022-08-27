package demobreadshop.repository;

import demobreadshop.domain.Sale;
import demobreadshop.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findAllByType(Status debt);

    @Query(
            value = "SELECT SUM(whole_price) FROM sale",
            nativeQuery = true
    )
    double sumOfIncome();

    @Query(
            value = "SELECT SUM(debt_price) FROM sale",
            nativeQuery = true
    )
    Double sumOfDebt();
}
