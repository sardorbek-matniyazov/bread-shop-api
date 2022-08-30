package demobreadshop.repository;

import demobreadshop.domain.Outcome;
import demobreadshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutcomeRepository extends JpaRepository<Outcome, Long> {
    @Query(
            value = "SELECT SUM(outcome_amount) FROM outcome WHERE outcome_type = ?1",
            nativeQuery = true
    )
    Double sumByType(String name);

    List<Outcome> findAllByUserId(Long user_id);
}
