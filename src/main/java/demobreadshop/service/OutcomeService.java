package demobreadshop.service;

import demobreadshop.domain.Outcome;
import demobreadshop.domain.enums.OutcomeType;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.OutcomeDto;

import java.util.List;

public interface OutcomeService {
    MyResponse create(OutcomeDto dto);

    List<Outcome> getAll();

    OutcomeType[] getTypes();

    MyResponse delete(long id);
}
