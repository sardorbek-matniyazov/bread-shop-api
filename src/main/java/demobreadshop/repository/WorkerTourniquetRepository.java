package demobreadshop.repository;

import demobreadshop.domain.WorkerTourniquet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface WorkerTourniquetRepository extends JpaRepository<WorkerTourniquet, Long> {
    List<WorkerTourniquet> findAllByOrderByIdDesc();

    @Modifying
    @Query(
            value = "update worker_tourniquet set updated_at = ?1 where updated_at = created_at",
            nativeQuery = true
    )
    Integer setEndedDate(Timestamp endedDate);
}
