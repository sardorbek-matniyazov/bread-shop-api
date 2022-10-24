package demobreadshop.service;

import demobreadshop.domain.Input;
import demobreadshop.domain.projection.InputDataProjection;
import demobreadshop.payload.InputDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.WorkerAccessDto;

import java.util.List;

public interface InputService {

    List<InputDataProjection> getAll();

    Input get(long id);

    List<Input> getAllWarehouseInputs();

    MyResponse create(InputDto dto);

    MyResponse delete(long id);

    MyResponse setAdminAccess(WorkerAccessDto dto);

    MyResponse setAdminNonAccess();
}
