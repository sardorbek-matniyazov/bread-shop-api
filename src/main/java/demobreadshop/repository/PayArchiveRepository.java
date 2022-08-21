package demobreadshop.repository;

import demobreadshop.domain.PayArchive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayArchiveRepository extends JpaRepository<PayArchive, Long> {
    List<PayArchive> findAllBySaleId(long id);
}
