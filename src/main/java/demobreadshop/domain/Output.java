package demobreadshop.domain;

import demobreadshop.domain.base.BaseInput;
import demobreadshop.domain.enums.OutputType;
import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
@AllArgsConstructor
public class Output extends BaseInput {

    @Enumerated(EnumType.STRING)
    private OutputType status;

    public Output() {
    }
}
