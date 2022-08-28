package demobreadshop.constants;

import demobreadshop.domain.enums.OutcomeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface ConstProperties {
    int DELETE_TIME = 30;
    char OPERATOR_MINUS = '-';
    char OPERATOR_PLUS = '+';
    List<OutcomeType> OUTCOME_TYPES = new ArrayList<>(Arrays.asList(OutcomeType.values()));
}
