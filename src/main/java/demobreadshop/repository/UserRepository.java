package demobreadshop.repository;

import demobreadshop.domain.User;
import demobreadshop.domain.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByFullName(String fullName);

    User findByFullName(String createdBy);

    boolean existsByFullNameAndIdIsNot(String fullName, long id);

    boolean existsByPhoneNumberAndIdIsNot(String phoneNumber, long id);

    @Modifying
    @Query(
            value = "update users set user_access = true where id = ?1 or id = ?2 ;",
            nativeQuery = true
    )
    Integer setAccessUser(Long id, Long id1);

    @Modifying
    @Query(
            value = "update users set user_access = false where true ;",
            nativeQuery = true
    )
    Integer setAccessUserFalse();
}
