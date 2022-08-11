package demobreadshop.repository;

import demobreadshop.domain.WareHouse;
import demobreadshop.domain.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WareHouseRepository extends JpaRepository<WareHouse, Long> {

    List<WareHouse> findAllByType(ProductType type);

    boolean existsByName(String name);
    boolean existsByNameAndIdIsNot(String name, long id);
}
