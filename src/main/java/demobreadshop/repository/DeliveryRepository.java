package demobreadshop.repository;

import demobreadshop.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Delivery findByDelivererId(Long id);
}
