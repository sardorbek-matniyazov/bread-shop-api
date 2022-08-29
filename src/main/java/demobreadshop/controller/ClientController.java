package demobreadshop.controller;

import demobreadshop.domain.Client;
import demobreadshop.payload.ClientDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@PreAuthorize(value = "hasAnyAuthority({'GL_ADMIN', 'SELLER_CAR', 'SELLER_ADMIN', 'WORKER'})")
@RequestMapping(value = "api/client")
public class ClientController {
    private final ClientService service;

    @Autowired
    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping(value = "/all")
    public HttpEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(value = "/{id}")
    public HttpEntity<?> get(@PathVariable(value = "id") long id) {
        Client get = service.get(id);
        return get != null
                ? ResponseEntity.ok(get)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping(value = "/create")
    public HttpEntity<?> create(@RequestBody @Valid ClientDto dto) {
        MyResponse create = service.create(dto);
        return create.isActive()
                ? ResponseEntity.ok(create)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(create);
    }

    @PutMapping(value = "/{id}")
    public HttpEntity<?> update(@PathVariable(value = "id") long id,
                                @RequestBody @Valid ClientDto dto) {
        MyResponse update = service.update(id, dto);
        return update.isActive()
                ? ResponseEntity.ok(update)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(update);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<?> checkValidation(MethodArgumentNotValidException e) {
        return AuthController.checkValidation(e);
    }
}
