package demobreadshop.controller;

import demobreadshop.domain.Delivery;
import demobreadshop.payload.DeliveryDto;
import demobreadshop.payload.MyResponse;
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
@PreAuthorize(value = "hasAnyAuthority({'GL_ADMIN', 'SELLER_CAR', 'SELLER_ADMIN', 'WORKER'})")
@RequestMapping(value = "/api/delivery")
public class DeliveryController {
    private final DeliveryService service;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.service = deliveryService;
    }

    @GetMapping(value = "/all")
    public HttpEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(value = "/{id}")
    public HttpEntity<?> get(@PathVariable(value = "id") long id) {
        Delivery get = service.get(id);
        return get != null
                ? ResponseEntity.ok(get)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping(value = "/{id}/balance")
    public HttpEntity<?> getBalance(@PathVariable(value = "id") long id) {
        return ResponseEntity.ok(service.getBalance(id));
    }

    @GetMapping(value = "/allOutputs")
    public HttpEntity<?> getAllDeliveries() {
        return ResponseEntity.ok(service.getAllDeliveries());
    }

    @PreAuthorize(value = "hasAnyAuthority({'SELLER_CAR'})")
    @GetMapping(value = "/currentBalance")
    public HttpEntity<?> getAllCurrentBalance() {
        return ResponseEntity.ok(service.getCurrentBalance());
    }

    @GetMapping(value = "/{id}/outputs")
    public HttpEntity<?> getAllDeliveriesWithId(@PathVariable(value = "id") long id) {
        return ResponseEntity.ok(service.getDeliveries(id));
    }

    @GetMapping(value = "/{id}/returns")
    public HttpEntity<?> getAllDeliveryReturns(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAllReturns(id));
    }

    @PreAuthorize(value = "hasAnyAuthority({'SELLER_CAR', 'SELLER_ADMIN'})")
    @PostMapping(value = "/createOutput")
    public HttpEntity<?> deliver(@RequestBody @Valid DeliveryDto dto) {
        MyResponse deliver = service.deliver(dto);
        return deliver.isActive()
                ? ResponseEntity.ok(deliver)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(deliver);
    }

    @PostMapping(value = "/returnProduct")
    public HttpEntity<?> returnProduct() {
        MyResponse info = service.returnProduct();
        return info.isActive()
                ? ResponseEntity.ok(info)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(info);
    }

    @DeleteMapping(value = "/output/{id}")
    public HttpEntity<?> deleteOutput(@PathVariable(value = "id") long id) {
        MyResponse delete = service.deleteOutputWithId(id);
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
