package demobreadshop.controller;

import demobreadshop.payload.ComplaintDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/complaint")
public class ComplaintController {

    private final ComplaintService service;

    @Autowired
    public ComplaintController(ComplaintService service) {
        this.service = service;
    }

    @GetMapping(value = "all")
    public HttpEntity<?> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping(value = "create")
    public HttpEntity<?> create(@RequestBody @Valid ComplaintDto dto, MultipartFile file) {
        MyResponse create = service.create(dto, file);
        return create.isActive()
                ? ResponseEntity.ok(create)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(create);
    }
}
