package demobreadshop.controller;

import demobreadshop.domain.enums.ProductType;
import demobreadshop.domain.enums.RoleName;
import demobreadshop.service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

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

    @GetMapping(value = "client-statistics")
    public HttpEntity<?> clientStatistics(@RequestParam(value = "start") String start, @RequestParam(value = "end") String end){
        return ResponseEntity.ok(service.clientStat(start, end));
    }

    @GetMapping(value = "car-seller")
    public HttpEntity<?> getAllCarSellerInfo() {
        return ResponseEntity.ok(service.getAllSellerInfo(RoleName.SELLER_CAR));
    }

    @GetMapping(value = "income-history/{id}")
    public HttpEntity<?> getAllIncomeHistoryInfo(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAllIncomeHistoryInfo(id));
    }

    @GetMapping(value = "outcome-history/{id}")
    public HttpEntity<?> getAllOutcomeHistoryInfo(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAllOutcomeHistoryInfo(id));
    }

    @GetMapping(value = "allClientIncome")
    public HttpEntity<?> getAllClientIncome() {
        return ResponseEntity.ok(service.getAllClientIncome());
    }

    @GetMapping(value = "finance")
    public HttpEntity<?> getFinance() {
        return ResponseEntity.ok(service.getFinanceInfo());
    }

    @GetMapping(value = "admin-seller")
    public HttpEntity<?> getAllAdminSellerInfo() {
        return ResponseEntity.ok(service.getAllSellerInfo(RoleName.SELLER_ADMIN));
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
        return ResponseEntity.ok(service.getAllProductStatistics(ProductType.PRODUCT));
    }

    @GetMapping(value = "material-statistics")
    public HttpEntity<?> getAllMaterialStatistics() {
        return ResponseEntity.ok(service.getAllProductStatistics(ProductType.MATERIAL));
    }

    @GetMapping(value = "seller-statistics")
    public HttpEntity<?> getAllSellerStatistics() {
        return ResponseEntity.ok(service.getAllSellerStatistics());
    }

    @GetMapping(value = "sale-info")
    public HttpEntity<?> getAllSaleInfoStatistics() {
        return ResponseEntity.ok(service.getSaleInfo());
    }


}
