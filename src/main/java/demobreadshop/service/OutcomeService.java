package demobreadshop.service;

import demobreadshop.domain.Outcome;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.OutcomeDto;

import java.util.List;
import java.util.Map;

public interface OutcomeService {
    MyResponse create(OutcomeDto dto);

    List<Outcome> getAll(String start, String end);

    Map<String, String> getTypes();

    MyResponse delete(long id);
}
