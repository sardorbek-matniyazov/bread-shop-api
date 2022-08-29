package demobreadshop.controller;

import demobreadshop.domain.enums.RoleName;
import demobreadshop.service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize(value = "hasAnyAuthority({'GL_ADMIN', 'SELLER_CAR', 'SELLER_ADMIN', 'WORKER'})")
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

    @GetMapping(value = "car-seller")
    public HttpEntity<?> getAllCarSellerInfo() {
        return ResponseEntity.ok(service.getAllCarSellerInfo(RoleName.SELLER_CAR));
    }

    @GetMapping(value = "admin-seller")
    public HttpEntity<?> getAllAdminSellerInfo() {
        return ResponseEntity.ok(service.getAllCarSellerInfo(RoleName.SELLER_ADMIN));
    }

    @GetMapping(value = "material-decrease")
    public HttpEntity<?> getMaterialDecrease() {
        return ResponseEntity.ok(service.getAllMaterialDecrease());
    }

    @GetMapping(value = "group-statistics")
    public HttpEntity<?> getAllGroupStatistics() {
        return ResponseEntity.ok(service.getAllGroupStatistics());
    }

    @GetMapping(value = "warehouse-statistics")
    public HttpEntity<?> getAllWarehouseStatistics() {
        return ResponseEntity.ok(service.getAllWarehouseStatistics());
    }

    @GetMapping(value = "product-statistics")
    public HttpEntity<?> getAllProductStatistics() {
        return ResponseEntity.ok(service.getAllProductStatistics());
    }

    @GetMapping(value = "seller-statistics")
    public HttpEntity<?> getAllSellerStatistics() {
        return ResponseEntity.ok(service.getAllSellerStatistics());
    }

}
