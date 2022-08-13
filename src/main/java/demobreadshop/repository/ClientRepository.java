package demobreadshop.repository;

import demobreadshop.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByFullName(String name);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByFullNameAndIdIsNot(String fullName, long id);

    boolean existsByPhoneNumberAndIdIsNot(String phoneNumber, long id);
}
