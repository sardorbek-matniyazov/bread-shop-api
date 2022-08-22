package demobreadshop.service;

import demobreadshop.domain.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAll();

    Role get(long id);
}
