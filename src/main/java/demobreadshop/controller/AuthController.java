package demobreadshop.controller;

import demobreadshop.domain.User;
import demobreadshop.payload.*;
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
    public HttpEntity<?> login(@RequestBody @Valid LoginDto dto) {
        return service.login(dto);
    }

    @PreAuthorize(value = "hasAnyAuthority({'GL_ADMIN', 'SELLER_CAR', 'SELLER_ADMIN', 'WORKER'})")
    @GetMapping(value = "/allUsers")
    public HttpEntity<?> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @PreAuthorize(value = "hasAuthority('GL_ADMIN')")
    @PostMapping(value = "/register")
    public HttpEntity<?> registerUser(@RequestBody @Valid RegisterDto dto) {
        final MyResponse register = service.register(dto);
        return register.isActive()
                ? ResponseEntity.ok(register)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(register);
    }

    @PreAuthorize(value = "hasAnyAuthority({'GL_ADMIN', 'SELLER_CAR', 'SELLER_ADMIN', 'WORKER'})")
    @PutMapping(value = "/{id}")
    public HttpEntity<?> update(@PathVariable long id, @RequestBody @Valid User dto) {
        MyResponse update = service.update(id, dto);
        return update.isActive()
                ? ResponseEntity.ok(update)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(update);
    }

    @PreAuthorize(value = "hasAnyAuthority({'GL_ADMIN', 'SELLER_CAR', 'SELLER_ADMIN', 'WORKER'})")
    @GetMapping(value = "/me")
    public HttpEntity<?> me() {
        final User me = service.me();
        return me != null
                ? ResponseEntity.ok(me)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MyResponse.USER_NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static HttpEntity<?> checkValidation(MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            message.append(errorMessage).append(" ");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResult(message.toString()));
    }
}
