package demobreadshop.repository;

import demobreadshop.domain.PayArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface PayArchiveRepository extends JpaRepository<PayArchive, Long> {
    List<PayArchive> findAllBySaleId(long id);

    @Query(
            value = "SELECT SUM(archive_amount) FROM pay_archive where pay_type = ?1",
            nativeQuery = true
    )
    Double sumOfAllCash(String name);

    @Query(
            value = "select sum(pa.archive_amount) as amount " +
                    "from pay_archive pa " +
                    "where pa.created_at >= ?1 and pa.created_at <= ?2",
            nativeQuery = true
    )
    Double findAllIncomeAmount(Timestamp timestamp, Timestamp timestamp1);
}
