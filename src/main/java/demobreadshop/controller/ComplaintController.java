package demobreadshop.controller;

import demobreadshop.payload.ComplaintDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "api/complaint")
public class ComplaintController {

    private final ComplaintService service;

    @Autowired
    public ComplaintController(ComplaintService service) {
        this.service = service;
    }

    @PreAuthorize(value = "hasAnyAuthority({'GL_ADMIN', 'SELLER_CAR', 'SELLER_ADMIN', 'WORKER', 'SUPERVISOR'})")
    @GetMapping(value = "all")
    public HttpEntity<?> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize(value = "hasAnyAuthority({'GL_ADMIN', 'SELLER_CAR', 'SELLER_ADMIN', 'WORKER', 'SUPERVISOR'})")
    @GetMapping(value = "{id}")
    public HttpEntity<?> get(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(service.get(id));
    }

    @PreAuthorize(value = "hasAnyAuthority({'GL_ADMIN', 'SUPERVISOR', 'SELLER_CAR', 'SELLER_ADMIN'})")
    @PostMapping(value = "create")
    public HttpEntity<?> create(ComplaintDto dto, MultipartFile file) {
        System.out.println(file);
        System.out.println(dto);
        MyResponse create = service.create(dto, file);
        return create.isActive()
                ? ResponseEntity.ok(create)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(create);
    }

    @PreAuthorize(value = "hasAnyAuthority({'GL_ADMIN', 'SUPERVISOR'})")
    @DeleteMapping(value = "{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        MyResponse delete = service.delete(id);
        return delete.isActive()
                ? ResponseEntity.ok(delete)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(delete);
    }

    @GetMapping(value = "getPhoto/{fileName}")
    public void getPhoto(@PathVariable String fileName, HttpServletResponse response){
        service.downloadPhoto(fileName, response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<?> checkValidation(MethodArgumentNotValidException e) {
        return AuthController.checkValidation(e);
    }
}
