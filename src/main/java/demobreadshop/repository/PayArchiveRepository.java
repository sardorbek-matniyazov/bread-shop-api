package demobreadshop.repository;

import demobreadshop.domain.PayArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PayArchiveRepository extends JpaRepository<PayArchive, Long> {
    List<PayArchive> findAllBySaleId(long id);

    @Query(
            value = "SELECT SUM(amount) from pay_archive",
            nativeQuery = true
    )
    Double sumOfAll();

    @Query(
            value = "SELECT SUM(amount) FROM pay_archive where type = ?1",
            nativeQuery = true
    )
    Double sumOfAllCash(String name);
}
