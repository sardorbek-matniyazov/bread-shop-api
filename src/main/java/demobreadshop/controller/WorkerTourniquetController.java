package demobreadshop.controller;

import demobreadshop.service.WorkerTourniquetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/workerTourniquet")
@PreAuthorize(value = "hasAnyAuthority({'GL_ADMIN', 'SELLER_ADMIN'})")
public class WorkerTourniquetController {
    private final WorkerTourniquetService service;

    @Autowired
    public WorkerTourniquetController(WorkerTourniquetService service) {
        this.service = service;
    }

    @GetMapping(value = "/findAll")
    public HttpEntity<?> getAllWorkerTourniquet() {
        return ResponseEntity.ok(service.getAllWorkerTourniquet());
    }

}
