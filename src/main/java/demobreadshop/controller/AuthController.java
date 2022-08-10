package demobreadshop.controller;

import demobreadshop.payload.ErrorResult;
import demobreadshop.payload.LoginDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.RegisterDto;
import demobreadshop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/auth")
public class AuthController {

    private final AuthService service;

    @Autowired
    public AuthController(AuthService authService) {
        this.service = authService;
    }

    @PostMapping(value = "/login")
    public HttpEntity<?> login(@RequestBody @Valid LoginDto dto){
        MyResponse login = service.login(dto);
        return login.isActive()
                ? ResponseEntity.ok(login)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(login);
    }

    @PreAuthorize(value = "hasAuthority('GL_ADMIN')")
    @PostMapping(value = "register")
    public HttpEntity<?> registerUser(@RequestBody @Valid RegisterDto dto){
        final MyResponse register = service.register(dto);
        return register.isActive()
                ? ResponseEntity.ok(register)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(register);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static HttpEntity<?> checkValidation(MethodArgumentNotValidException e){
        StringBuilder message = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            message.append(errorMessage).append(" ");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResult(message.toString()));
    }
}
