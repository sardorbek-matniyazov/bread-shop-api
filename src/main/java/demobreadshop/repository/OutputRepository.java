package demobreadshop.repository;

import demobreadshop.domain.Output;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutputRepository extends JpaRepository<Output, Long> {
}
