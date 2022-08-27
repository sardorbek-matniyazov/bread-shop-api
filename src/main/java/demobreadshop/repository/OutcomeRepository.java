package demobreadshop.repository;

import demobreadshop.domain.Outcome;
import demobreadshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OutcomeRepository extends JpaRepository<Outcome, Long> {
    @Query(
            value = "SELECT SUM(amount) FROM outcome WHERE type = ?1",
            nativeQuery = true
    )
    Double sumByType(String name);

}
