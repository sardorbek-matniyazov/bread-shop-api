package demobreadshop.domain.base;

import demobreadshop.security.SpringSecurityAuditAwareImpl;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;

@MappedSuperclass
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@EntityListeners(SpringSecurityAuditAwareImpl.class)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    @CreatedDate
    private Timestamp createdAt;

    @UpdateTimestamp
    @LastModifiedDate
    private Timestamp updatedAt;

    private long createdBy;
}
