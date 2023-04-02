package demobreadshop.repository;

import demobreadshop.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Delivery findByDelivererId(Long id);

    @Query(
            value = "SELECT * FROM delivery ORDER BY id DESC LIMIT 1000",
            nativeQuery = true
    )
    List<Delivery> findAllByOrderByIdDesc();
    Delivery findByDeliverer_FullName(String deliverer_fullName);
}
