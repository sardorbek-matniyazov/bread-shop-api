package demobreadshop.repository;

import demobreadshop.domain.Input;
import demobreadshop.domain.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InputRepository extends JpaRepository<Input, Long> {
    List<Input> findAllByType(ProductType type);
}
