package demobreadshop.service.impl;

import demobreadshop.domain.Client;
import demobreadshop.domain.WareHouse;
import demobreadshop.payload.ClientDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.repository.ClientRepository;
import demobreadshop.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repository;

    @Autowired
    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public Client get(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Client> getAll() {
        return repository.findAll();
    }

    @Override
    public MyResponse create(ClientDto dto) {
        if (repository.existsByFullName(dto.getName()))
            return MyResponse.FULL_NAME_EXISTS;

        if (repository.existsByPhoneNumber(dto.getPhoneNumber()))
            return MyResponse.PHONE_NUMBER_EXISTS;

        repository.save(
                new Client(
                        dto.getName(),
                        dto.getPhoneNumber(),
                        dto.getComment()
                )
        );
        return MyResponse.SUCCESSFULLY_CREATED;
    }

    @Override
    public MyResponse update(long id, ClientDto dto) {
        if (repository.existsByFullNameAndIdIsNot(dto.getName(), id))
            return MyResponse.FULL_NAME_EXISTS;

        if (repository.existsByPhoneNumberAndIdIsNot(dto.getPhoneNumber(), id))
            return MyResponse.PHONE_NUMBER_EXISTS;

        repository.save(
                new Client(
                        id,
                        dto.getName(),
                        dto.getPhoneNumber(),
                        dto.getComment()
                )
        );
        return MyResponse.SUCCESSFULLY_UPDATED;
    }

    @Override
    public MyResponse delete(long id) {
        final Optional<Client> byId = repository.findById(id);
        if (byId.isPresent()) {
            try {
                final Client client = byId.get();
                if (AuthServiceImpl.isNonDeletable(client.getCreatedAt().getTime())) {
                    return MyResponse.CANT_DELETE;
                }
                repository.deleteById(id);
                return MyResponse.SUCCESSFULLY_DELETED;
            } catch (HibernateException e) {
                log.info(e.getMessage());
            }
            return MyResponse.CANT_DELETE;
        }
        return MyResponse.CLIENT_NOT_FOUND;
    }
}
