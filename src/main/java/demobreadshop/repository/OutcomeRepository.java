package demobreadshop.repository;

import demobreadshop.domain.Outcome;
import demobreadshop.domain.projection.OutcomeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface OutcomeRepository extends JpaRepository<Outcome, Long> {
    @Query(
            value = "SELECT SUM(outcome_amount) FROM outcome WHERE outcome_type = ?1 and created_at >= ?2 and created_at <= ?3",
            nativeQuery = true
    )
    Double sumByType(String name, Timestamp time, Timestamp timestamp);

    @Query(
            value = "select * from outcome where user_id = ?1 and created_at >= ?2 and created_at <= ?3\n",
            nativeQuery = true
    )
    List<Outcome> getAllByUsersId(Long user_id, Timestamp time, Timestamp timestamp);

    @Query(
            value = "select sum(o.outcome_amount) as amount " +
                    "from outcome o " +
                    "where o.created_at >= ?1 and o.created_at <= ?2",
            nativeQuery = true
    )
    Double sumAllOutcome(Timestamp timestamp, Timestamp timestamp1);

    @Query(
            value = "with bum as (select o.created_at as createdAt, o.id as id, o.outcome_amount as amount, o.created_by as createdBy, o.outcome_type as type, u2.role_name as roleName, o.user_id as user_id " +
                    "             from outcome o join ((users u join users_roles ur on u.id = ur.users_id) d join role r on d.roles_id = r.id) u2 on o.created_by = u2.full_name " +
                    "    where o.created_at >= ?1 and o.created_at <= ?2) " +
                    "select bum.createdAt, bum.amount, bum.createdBy, bum.type, bum.roleName, u3.full_name as user " +
                    "from users u3 right join bum on bum.user_id = u3.id",
            nativeQuery = true
    )
    List<OutcomeProjection> findAllWithRoleName(Timestamp start, Timestamp end);

    List<Outcome> findAllByCreatedAtBetween(Timestamp start, Timestamp end);
}
