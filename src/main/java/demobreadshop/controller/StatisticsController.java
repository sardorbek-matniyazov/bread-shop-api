package demobreadshop.controller;

import demobreadshop.domain.enums.ProductType;
import demobreadshop.domain.enums.RoleName;
import demobreadshop.service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public HttpEntity<?> outcomeStatistics(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end){
        return ResponseEntity.ok(service.outcomeStat(start, end));
    }

    @GetMapping(value = "client-statistics")
    public HttpEntity<?> clientStatistics(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end){
        return ResponseEntity.ok(service.clientStat(start, end));
    }

    @GetMapping(value = "car-seller")
    public HttpEntity<?> getAllCarSellerInfo(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllSellerInfo(RoleName.SELLER_CAR, start, end));
    }

    @GetMapping(value = "income-history/{id}")
    public HttpEntity<?> getAllIncomeHistoryInfo(@PathVariable Long id, @RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllIncomeHistoryInfo(id, start, end));
    }

    @GetMapping(value = "outcome-history/{id}")
    public HttpEntity<?> getAllOutcomeHistoryInfo(@PathVariable Long id, @RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllOutcomeHistoryInfo(id, start, end));
    }

    @GetMapping(value = "car-seller/{id}")
    public HttpEntity<?> getAllCarSellerSaleInfo(@PathVariable Long id, @RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllSellerSaleInfo(id, start, end));
    }

    @GetMapping(value = "admin-seller/{id}")
    public HttpEntity<?> getAllAdminSellerSaleInfo(@PathVariable Long id, @RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllSellerSaleInfo(id, start, end));
    }

    @GetMapping(value = "client-sale/{id}")
    public HttpEntity<?> getAllClientSaleInfo(@PathVariable Long id, @RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllClientSaleInfo(id, start, end));
    }

    @GetMapping(value = "allClientIncome")
    public HttpEntity<?> getAllClientIncome(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllClientIncome(start, end));
    }

    @GetMapping(value = "benefit-input")
    public HttpEntity<?> getAllBenefits(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllBenefits(start, end));
    }

    @GetMapping(value = "clientsDebt")
    public HttpEntity<?> getAllClientsDebt() {
        return ResponseEntity.ok(service.getAllClientsDebt());
    }

    @GetMapping(value = "benefit-sale")
    public HttpEntity<?> getAllSale(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllSale(start, end));
    }

    @GetMapping(value = "finance")
    public HttpEntity<?> getFinance(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getFinanceInfo(start, end));
    }

    @GetMapping(value = "admin-seller")
    public HttpEntity<?> getAllAdminSellerInfo(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllSellerInfo(RoleName.SELLER_ADMIN, start, end));
    }

    @GetMapping(value = "finance-client")
    public HttpEntity<?> getAllClientPaidSum(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllClientPaidSum(start, end));
    }

    @GetMapping(value = "material-decrease")
    public HttpEntity<?> getMaterialDecrease(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllMaterialDecrease(start, end));
    }

    @GetMapping(value = "group-statistics")
    public HttpEntity<?> getAllGroupStatistics(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllGroupStatistics(start, end));
    }

    @GetMapping(value = "warehouse-statistics")
    public HttpEntity<?> getAllWarehouseStatistics(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllWarehouseStatistics(start, end));
    }

    @GetMapping(value = "product-statistics")
    public HttpEntity<?> getAllProductStatistics(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllProductStatistics(ProductType.PRODUCT, start, end));
    }

    @GetMapping(value = "material-statistics")
    public HttpEntity<?> getAllMaterialStatistics(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllProductStatistics(ProductType.MATERIAL, start, end));
    }

    @GetMapping(value = "seller-statistics")
    public HttpEntity<?> getAllSellerStatistics(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getAllSellerStatistics(start, end));
    }

    @GetMapping(value = "sale-info")
    public HttpEntity<?> getAllSaleInfoStatistics(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        return ResponseEntity.ok(service.getSaleInfo(start, end));
    }
}
