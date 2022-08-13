package demobreadshop.controller;

import demobreadshop.domain.WareHouse;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.ProductDto;
import demobreadshop.service.ProductService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/product")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService productService) {
        this.service = productService;
    }

    @PreAuthorize(value = "hasAnyAuthority('GL_ADMIN')")
    @GetMapping(value = "/all")
    public HttpEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize(value = "hasAnyAuthority('GL_ADMIN')")
    @GetMapping(value = "/{id}")
    public HttpEntity<?> get(@PathVariable(value = "id") long id) {
        WareHouse get = service.get(id);
        return get != null
                ? ResponseEntity.ok(get)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PreAuthorize(value = "hasAnyAuthority('GL_ADMIN')")
    @GetMapping(value = "/warehouse")
    public HttpEntity<?> getAllWarehouse() {
        return ResponseEntity.ok(service.getAllWarehouseProducts());
    }

    @PreAuthorize(value = "hasAnyAuthority('GL_ADMIN')")
    @PostMapping(value = "/create")
    public HttpEntity<?> login(@RequestBody @Valid ProductDto dto) {
        MyResponse create = service.create(dto);
        return create.isActive()
                ? ResponseEntity.ok(create)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(create);
    }

    @PreAuthorize(value = "hasAnyAuthority('GL_ADMIN')")
    @PutMapping(value = "/{id}")
    public HttpEntity<?> update(@PathVariable(value = "id") long id,
                                @RequestBody @Valid ProductDto dto) {
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
