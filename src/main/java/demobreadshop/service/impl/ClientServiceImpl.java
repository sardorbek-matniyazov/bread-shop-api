package demobreadshop.service.impl;

import demobreadshop.domain.Client;
import demobreadshop.payload.ClientDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.repository.ClientRepository;
import demobreadshop.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.transaction.Transactional;
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
        return repository.findAllByOrderByFullNameDesc();
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
                        dto.getComment(),
                        dto.isKindergarten()
                )
        );
        return MyResponse.SUCCESSFULLY_CREATED;
    }

    @Override
    public MyResponse update(long id, ClientDto dto) {
        final Optional<Client> byId = repository.findById(id);
        if (byId.isPresent()) {
            if (repository.existsByFullNameAndIdIsNot(dto.getName(), id))
                return MyResponse.FULL_NAME_EXISTS;

            if (repository.existsByPhoneNumberAndIdIsNot(dto.getPhoneNumber(), id))
                return MyResponse.PHONE_NUMBER_EXISTS;

            Client client = byId.get();
            client.setComment(dto.getComment());
            client.setFullName(dto.getName());
            client.setPhoneNumber(dto.getPhoneNumber());
            client.setKindergarten(dto.isKindergarten());
            repository.save(client);
            return MyResponse.SUCCESSFULLY_UPDATED;
        }

        return MyResponse.CLIENT_NOT_FOUND;
    }

    @Override
    @Transactional
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
                return MyResponse.CANT_DELETE;
            }
        }
        return MyResponse.CLIENT_NOT_FOUND;
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public static MyResponse handleHibernateException(DataIntegrityViolationException ex) {
        log.warn(ex.getMessage());
        return MyResponse.CANT_DELETE;
    }
}
