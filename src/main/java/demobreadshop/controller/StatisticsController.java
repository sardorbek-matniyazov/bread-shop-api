package demobreadshop.controller;

import demobreadshop.service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize(value = "hasAnyAuthority({'GL_ADMIN', 'SELLER_CAR'})")
@RequestMapping(value = "api/statistics")
public class StatisticsController {
    private final ArchiveService service;

    @Autowired
    public StatisticsController(ArchiveService service) {
        this.service = service;
    }

    @GetMapping(value = "all")
    public HttpEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(value = "outcome")
    public HttpEntity<?> outcomeStatistics(){
        return ResponseEntity.ok(service.outcomeStat());
    }
}
