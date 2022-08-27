package demobreadshop.service.impl;

import demobreadshop.domain.Outcome;
import demobreadshop.domain.User;
import demobreadshop.domain.enums.OutcomeType;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.OutcomeDto;
import demobreadshop.repository.OutcomeRepository;
import demobreadshop.repository.UserRepository;
import demobreadshop.service.AuthService;
import demobreadshop.service.OutcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
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
        if (outcomeType.equals(OutcomeType.SALARY)) {
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
    public List<Outcome> getAll() {
        return repository.findAll();
    }

    @Override
    public OutcomeType[] getTypes() {
        return OutcomeType.values();
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
                if (outcome.getType().equals(OutcomeType.SALARY)) {
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
