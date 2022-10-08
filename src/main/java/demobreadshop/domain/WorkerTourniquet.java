package demobreadshop.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import demobreadshop.domain.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "worker_tourniquet")
@NoArgsConstructor
@AllArgsConstructor
public class WorkerTourniquet extends BaseEntity {
    @ManyToOne()
    private User workerOne;

    @ManyToOne()
    private User workerTwo;

    @JsonValue()
    public Map<String, Object> toJson() {
        Map<String, Object> response = new HashMap<>();
        response.put("workerOne", this.workerOne.getFullName());
        response.put("workerTwo", this.workerTwo.getFullName());
        response.put("startedAt", super.getCreatedAt());
        if (super.getCreatedAt().equals(super.getUpdatedAt())) {
            response.put("endedAt", null);
        } else {
            response.put("endedAt", super.getUpdatedAt());
        }
        return response;
    }
}
