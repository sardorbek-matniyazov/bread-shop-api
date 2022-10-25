package demobreadshop.service.impl;

import demobreadshop.constants.ConstProperties;
import demobreadshop.domain.Outcome;
import demobreadshop.domain.User;
import demobreadshop.domain.enums.OutcomeType;
import demobreadshop.domain.enums.RoleName;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.OutcomeDto;
import demobreadshop.repository.OutcomeRepository;
import demobreadshop.repository.UserRepository;
import demobreadshop.service.OutcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OutcomeServiceImpl implements OutcomeService {
    private final OutcomeRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public OutcomeServiceImpl(OutcomeRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public MyResponse create(OutcomeDto dto) {
        OutcomeType outcomeType = OutcomeType.valueOf(dto.getType());
        Outcome outcome = new Outcome(dto.getMoneyAmount(), outcomeType, dto.getComment());
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (outcomeType.equals(OutcomeType.OYLIK)) {
            if (principal.getRoles().stream().noneMatch(r -> r.getRoleName().equals(RoleName.GL_ADMIN) || r.getRoleName().equals(RoleName.SELLER_ADMIN))) {
                return MyResponse.YOU_CANT_CREATE;
            }
            Optional<User> byId = userRepository.findById(dto.getUserId());
            if (byId.isPresent()) {
                User user = byId.get();
                user.setBalance(user.getBalance() - dto.getMoneyAmount());
                outcome.setUser(userRepository.save(user));
            } else {
                return MyResponse.USER_NOT_FOUND;
            }
        }
        repository.save(outcome);
        return MyResponse.SUCCESSFULLY_CREATED;
    }

    @Override
    public List<Outcome> getAll(String start, String end) {
        if (start == null && end == null) {
            return repository.findAllByCreatedAtBetweenOrderByIdDesc(new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
        }
        else {
            return repository.findAllByCreatedAtBetweenOrderByIdDesc(ArchiveServiceImpl.getTime(start), ArchiveServiceImpl.getTime(end));
        }
    }

    @Override
    public Map<String, String> getTypes() {
        Map<String, String> types = new HashMap<>();
        for (OutcomeType value : OutcomeType.values()) {
            types.put("name", value.name());
        }
        return types;
    }

    @Override
    public MyResponse delete(long id) {
        Optional<Outcome> byId = repository.findById(id);
        if (byId.isPresent()) {
            User creator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Outcome outcome = byId.get();
            if (AuthServiceImpl.isNonDeletable(outcome.getCreatedAt().getTime())) {
                return MyResponse.CANT_DELETE;
            }
            if (outcome.getCreatedBy().equals(creator.getFullName())) {
                if (outcome.getType().equals(OutcomeType.OYLIK)) {
                    User user = outcome.getUser();
                    user.setBalance(user.getBalance() + outcome.getAmount());
                    userRepository.save(user);
                }
                repository.delete(outcome);
                return MyResponse.SUCCESSFULLY_DELETED;
            }
            return MyResponse.CAN_DELETE_OWN;
        }
        return MyResponse.OUTCOME_NOT_FOUND;
    }
}
