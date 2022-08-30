package demobreadshop.repository;

import demobreadshop.domain.Output;
import demobreadshop.domain.enums.OutputType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutputRepository extends JpaRepository<Output, Long> {
    List<Output> findAllByDeliveryId(Long delivery_id);

    List<Output> findAllByTypeOrderByCreatedAtDesc(OutputType oDeliverer);
}
