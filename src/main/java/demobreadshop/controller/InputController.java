package demobreadshop.controller;

import demobreadshop.domain.Input;
import demobreadshop.domain.WareHouse;
import demobreadshop.payload.InputDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.ProductDto;
import demobreadshop.service.InputService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/input")
public class InputController {
    private final InputService service;

    public InputController(InputService service) {
        this.service = service;
    }

    @PreAuthorize(value = "hasAnyAuthority('GL_ADMIN')")
    @GetMapping(value = "/all")
    public HttpEntity<?> getAll(){
        return ResponseEntity.ok(service.getAll());
    }


    @PreAuthorize(value = "hasAnyAuthority('GL_ADMIN')")
    @GetMapping(value = "/{id}")
    public HttpEntity<?> get(@PathVariable(value = "id") long id){
        Input get = service.get(id);
        return get != null
                ? ResponseEntity.ok(get)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PreAuthorize(value = "hasAnyAuthority('GL_ADMIN')")
    @GetMapping(value = "/warehouse")
    public HttpEntity<?> getAllWarehouse(){
        return ResponseEntity.ok(service.getAllWarehouseInputs());
    }

    @PreAuthorize(value = "hasAnyAuthority('GL_ADMIN')")
    @PostMapping(value = "/create")
    public HttpEntity<?> login(@RequestBody @Valid InputDto dto){
        MyResponse create = service.create(dto);
        return create.isActive()
                ? ResponseEntity.ok(create)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(create);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<?> checkValidation(MethodArgumentNotValidException e){
        return AuthController.checkValidation(e);
    }
}
