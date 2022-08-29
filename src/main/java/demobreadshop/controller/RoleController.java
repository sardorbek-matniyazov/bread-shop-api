package demobreadshop.controller;

import demobreadshop.domain.Role;
import demobreadshop.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize(value = "hasAnyAuthority({'GL_ADMIN', 'SELLER_CAR', 'SELLER_ADMIN', 'WORKER'})")
@RequestMapping(value = "api/role")
public class RoleController {

    private final RoleService service;

    @Autowired
    public RoleController(RoleService service) {
        this.service = service;
    }

    @GetMapping(value = "/all")
    public HttpEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(value = "/{id}")
    public HttpEntity<?> get(@PathVariable(value = "id") long id) {
        Role get = service.get(id);
        return get != null
                ? ResponseEntity.ok(get)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<?> checkValidation(MethodArgumentNotValidException e) {
        return AuthController.checkValidation(e);
    }
}
