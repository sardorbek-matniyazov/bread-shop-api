package demobreadshop.service;

import demobreadshop.domain.Client;
import demobreadshop.payload.ClientDto;
import demobreadshop.payload.MyResponse;

import java.util.List;

public interface ClientService {
    List<Client> getAll();

    MyResponse create(ClientDto dto);

    Client get(long id);

    MyResponse update(long id, ClientDto dto);

    MyResponse delete(long id);
}
