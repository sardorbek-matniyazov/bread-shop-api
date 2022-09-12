package demobreadshop.controller;

import demobreadshop.payload.MyResponse;
import demobreadshop.payload.OutcomeDto;
import demobreadshop.service.OutcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@PreAuthorize(value = "hasAnyAuthority({'GL_ADMIN', 'SELLER_CAR', 'SELLER_ADMIN', 'WORKER'})")
@RequestMapping(value = "/api/outcome")
public class OutcomeController {
    private final OutcomeService service;

    @Autowired
    public OutcomeController(OutcomeService outcomeService) {
        this.service = outcomeService;
    }

    @PostMapping(value = "create")
    public HttpEntity<?> create(@RequestBody @Valid OutcomeDto dto) {
        MyResponse create = service.create(dto);
        return create.isActive()
                ? ResponseEntity.ok(create)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(create);
    }

    // k
    @GetMapping(value = "all")
    public HttpEntity<?> all(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end){
        return ResponseEntity.ok(service.getAll(start, end));
    }

    @GetMapping(value = "types")
    public HttpEntity<?> types(){
        return ResponseEntity.ok(service.getTypes());
    }

    @DeleteMapping(value = "{id}")
    public HttpEntity<?> delete(@PathVariable long id) {
        MyResponse delete = service.delete(id);
        return delete.isActive()
                ? ResponseEntity.ok(delete)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(delete);
    }

}
