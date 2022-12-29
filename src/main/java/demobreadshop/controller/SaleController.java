package demobreadshop.controller;

import demobreadshop.domain.Sale;
import demobreadshop.domain.enums.PaymentStatus;
import demobreadshop.domain.enums.SaleStatus;
import demobreadshop.payload.DebtDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.PaymentDateDto;
import demobreadshop.payload.SaleDto;
import demobreadshop.service.SaleService;
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
@RequestMapping(value = "api/sale")
public class SaleController {
    private final SaleService service;

    @Autowired
    public SaleController(SaleService service) {
        this.service = service;
    }

    @GetMapping(value = "/all")
    public HttpEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(value = "/allDebt")
    public HttpEntity<?> getAllDebts() {
        return ResponseEntity.ok(service.getAllByType(SaleStatus.DEBT, false));
    }

    @GetMapping(value = "/allDebtKindergarten")
    public HttpEntity<?> getAllDebtsOfKindergarten() {
        return ResponseEntity.ok(service.getAllByType(SaleStatus.DEBT, true));
    }

    @PreAuthorize(value = "hasAuthority({'GL_ADMIN'})")
    @PutMapping(value = "/payment/{id}")
    public HttpEntity<?> checkPaymentArchive(@PathVariable Long id) {
        MyResponse check = service.checkPayment(id);
        return check.isActive()
                ? ResponseEntity.ok(check)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(check);
    }

    @GetMapping(value = "/payment/allWait")
    public HttpEntity<?> getAllWaitPayments() {
        return ResponseEntity.ok(service.getPaymentsByType(PaymentStatus.WAIT));
    }

    @GetMapping(value = "/allPayed")
    public HttpEntity<?> getAllPayed() {
        return ResponseEntity.ok(service.getAllByType(SaleStatus.PAYED, false));
    }

    @GetMapping(value = "/{id}")
    public HttpEntity<?> get(@PathVariable(value = "id") long id) {
        Sale get = service.get(id);
        return get != null
                ? ResponseEntity.ok(get)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping(value = "/{id}/archives")
    public HttpEntity<?> getArchives(@PathVariable(value = "id") long id) {
        return ResponseEntity.ok(service.getArchives(id));
    }

    @PreAuthorize(value = "hasAnyAuthority({'SELLER_CAR', 'SELLER_ADMIN'})")
    @PostMapping(value = "/sell")
    public HttpEntity<?> sell(@RequestBody @Valid SaleDto dto) {
        MyResponse sell = service.sell(dto);
        return sell.isActive()
                ? ResponseEntity.ok(sell)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sell);
    }

    @PreAuthorize(value = "hasAnyAuthority({'SELLER_ADMIN', 'SELLER_CAR', 'GL_ADMIN'})")
    @PostMapping(value = "/payDebt")
    public HttpEntity<?> payForDebt(@RequestBody @Valid DebtDto dto) {
        MyResponse sell = service.payForDebt(dto);
        return sell.isActive()
                ? ResponseEntity.ok(sell)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sell);
    }

    @PutMapping(value = "payment/update/{id}")
    public HttpEntity<?> updatePaymentWithPaymentId(@PathVariable Long id, PaymentDateDto dto) {
        return ResponseEntity.ok(service.updatePaymentDate(id, dto));
    }

    @DeleteMapping(value = "payment/{id}")
    public HttpEntity<?> deletePaymentWithId(@PathVariable Long id) {
        return ResponseEntity.ok(service.deletePayment(id));
    }

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
