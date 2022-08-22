package demobreadshop.controller;

import demobreadshop.domain.Delivery;
import demobreadshop.domain.Sale;
import demobreadshop.payload.DeliveryDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.SaleDto;
import demobreadshop.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/delivery")
public class DeliveryController {
    private final DeliveryService service;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.service = deliveryService;
    }

    @PreAuthorize(value = "hasAnyAuthority('GL_ADMIN')")
    @GetMapping(value = "/all")
    public HttpEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize(value = "hasAnyAuthority('GL_ADMIN')")
    @GetMapping(value = "/{id}")
    public HttpEntity<?> get(@PathVariable(value = "id") long id) {
        Delivery get = service.get(id);
        return get != null
                ? ResponseEntity.ok(get)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PreAuthorize(value = "hasAnyAuthority('GL_ADMIN')")
    @PostMapping(value = "/sell")
    public HttpEntity<?> sell(@RequestBody @Valid DeliveryDto dto) {
        MyResponse sell = service.deliver(dto);
        return sell.isActive()
                ? ResponseEntity.ok(sell)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sell);
    }

    @PreAuthorize(value = "hasAnyAuthority('GL_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public HttpEntity<?> delete(@PathVariable long id) {
        MyResponse delete = service.delete(id);
        return delete.isActive()
                ? ResponseEntity.ok(delete)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(delete);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<?> checkValidation(MethodArgumentNotValidException e) {
        return AuthController.checkValidation(e);
    }

}
