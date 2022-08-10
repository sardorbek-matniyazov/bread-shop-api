package demobreadshop.service;

import demobreadshop.payload.LoginDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.RegisterDto;

public interface AuthService {
    MyResponse login(LoginDto dto);

    MyResponse register(RegisterDto dto);
}
