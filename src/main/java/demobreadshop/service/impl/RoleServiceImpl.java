package demobreadshop.service.impl;

import demobreadshop.domain.Role;
import demobreadshop.repository.RoleRepository;
import demobreadshop.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Role> getAll() {
        return repository.findAll();
    }

    @Override
    public Role get(long id) {
        return repository.findById(id).orElse(null);
    }
}
