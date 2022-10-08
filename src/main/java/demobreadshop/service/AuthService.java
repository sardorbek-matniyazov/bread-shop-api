package demobreadshop.service;

import demobreadshop.domain.User;
import demobreadshop.domain.enums.RoleName;
import demobreadshop.payload.LoginDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.RegisterDto;
import org.springframework.http.HttpEntity;

import java.util.List;

public interface AuthService {
    HttpEntity<?> login(LoginDto dto);

    MyResponse register(RegisterDto dto);

    User me();

    MyResponse update(long id, User dto);

    List<User> getAllUsers();

    List<User> getAllUsersByRoleName(RoleName worker);
}
