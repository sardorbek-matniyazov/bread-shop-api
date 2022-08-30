package demobreadshop.service.impl;

import demobreadshop.domain.Role;
import demobreadshop.domain.enums.OutcomeType;
import demobreadshop.domain.enums.PayType;
import demobreadshop.domain.enums.ProductType;
import demobreadshop.domain.enums.RoleName;
import demobreadshop.domain.projection.*;
import demobreadshop.repository.*;
import demobreadshop.service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArchiveServiceImpl implements ArchiveService {
    private final SaleRepository saleRepository;
    private final PayArchiveRepository payArchiveRepository;
    private final OutcomeRepository outcomeRepository;
    private final RoleRepository roleRepository;
    private final InputRepository inputRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ArchiveServiceImpl(SaleRepository saleRepository, PayArchiveRepository payArchiveRepository, OutcomeRepository outcomeRepository, RoleRepository roleRepository, InputRepository inputRepository, ClientRepository clientRepository) {
        this.saleRepository = saleRepository;
        this.payArchiveRepository = payArchiveRepository;
        this.outcomeRepository = outcomeRepository;
        this.roleRepository = roleRepository;
        this.inputRepository = inputRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Map<String, Double> getAll() {
        Map<String, Double> map = new HashMap<>();
        map.put("WholeIncome", saleRepository.sumOfIncome());
        map.put("countClients", clientRepository.countAll());
        map.put("WholeDebt", saleRepository.sumOfDebt());
        map.put("Income", payArchiveRepository.sumOfAll());
        map.put("CashIncome", payArchiveRepository.sumOfAllCash(PayType.CASH.name()));
        map.put("CardIncome", payArchiveRepository.sumOfAllCash(PayType.CARD.name()));
        return map;
    }

    @Override
    public Map<String, Double> outcomeStat() {
        Map<String, Double> stat = new HashMap<>();
        for (OutcomeType type: OutcomeType.values()) {
            stat.put(type.name(), outcomeRepository.sumByType(type.name()));
        }
        return stat;
    }

    @Override
    public List<SaleStatistics> getAllSellerInfo(RoleName seller) {
        Role byRoleName = roleRepository.getByRoleName(seller);
        return saleRepository.getAllUserInfoByRoleId(byRoleName.getId());
    }

    @Override
    public List<MaterialDecreaseStat> getAllMaterialDecrease() {
        return saleRepository.getAllMaterialDecrease();
    }

    @Override
    public List<GroupStatistics> getAllGroupStatistics() {
        Role byRoleName = roleRepository.getByRoleName(RoleName.WORKER);
        return inputRepository.getAllGroupStatistics(byRoleName.getId());
    }

    @Override
    public List<InputStatistics> getAllProductStatistics() {
        return inputRepository.getAllInputStatistics(ProductType.PRODUCT.name());
    }

    @Override
    public List<InputStatistics> getAllWarehouseStatistics() {
        return inputRepository.getAllInputStatistics(ProductType.MATERIAL.name());
    }

    @Override
    public List<SellerStatistics> getAllSellerStatistics() {
        return saleRepository.getAllUserStatistics();
    }

    @Override
    public List<ClientStatistics> clientStat() {
        return saleRepository.getAllClientSale();
    }

    @Override
    public List<SaleInfoProjection> getSaleInfo() {
        return saleRepository.getSaleInfo();
    }
}
