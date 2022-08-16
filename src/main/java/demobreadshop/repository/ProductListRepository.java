package demobreadshop.repository;

import demobreadshop.domain.ProductList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductListRepository extends JpaRepository<ProductList, Long> {
}
