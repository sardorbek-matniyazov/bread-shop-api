package demobreadshop.repository;

import demobreadshop.domain.Outcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface OutcomeRepository extends JpaRepository<Outcome, Long> {
    @Query(
            value = "SELECT SUM(outcome_amount) FROM outcome WHERE outcome_type = ?1 and created_at >= ?2 and created_at <= ?3",
            nativeQuery = true
    )
    Double sumByType(String name, Timestamp time, Timestamp timestamp);

    @Query(
            value = "select * from outcome where user_id = ?1 and created_at >= ?2 and created_at <= ?3\n",
            nativeQuery = true
    )
    List<Outcome> getAllByUsersId(Long user_id, Timestamp time, Timestamp timestamp);
}
