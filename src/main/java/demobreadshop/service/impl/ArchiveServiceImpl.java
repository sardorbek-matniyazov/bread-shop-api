package demobreadshop.service.impl;

import demobreadshop.domain.Outcome;
import demobreadshop.domain.Role;
import demobreadshop.domain.User;
import demobreadshop.domain.enums.OutcomeType;
import demobreadshop.domain.enums.ProductType;
import demobreadshop.domain.enums.RoleName;
import demobreadshop.domain.projection.*;
import demobreadshop.repository.*;
import demobreadshop.service.ArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class ArchiveServiceImpl implements ArchiveService {
    private final SaleRepository saleRepository;
    private final PayArchiveRepository payArchiveRepository;
    private final OutcomeRepository outcomeRepository;
    private final RoleRepository roleRepository;
    private final InputRepository inputRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArchiveServiceImpl(SaleRepository saleRepository, PayArchiveRepository payArchiveRepository, OutcomeRepository outcomeRepository, RoleRepository roleRepository, InputRepository inputRepository, ClientRepository clientRepository, UserRepository userRepository) {
        this.saleRepository = saleRepository;
        this.payArchiveRepository = payArchiveRepository;
        this.outcomeRepository = outcomeRepository;
        this.roleRepository = roleRepository;
        this.inputRepository = inputRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, Double> getAll() {
        Map<String, Double> map = new HashMap<>();
        map.put("countClients", clientRepository.countAll());
        map.put("countDebt", saleRepository.countOfDebtClients().size() * 1.0);
        map.put("sumDebt", saleRepository.sumOfDebt());
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
    public List<InputStatistics> getAllProductStatistics(ProductType type) {
        return inputRepository.getAllInputStatistics(type.name());
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
    public List<ClientStatistics> clientStat(String start, String end) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01");
            Date go = new SimpleDateFormat("yyyy-MM-dd").parse("2022-12-12");
            long from = date.getTime();
            long to = go.getTime();
            return saleRepository.getAllClientSale(new Timestamp(from), new Timestamp(to));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SaleInfoProjection> getSaleInfo() {
        return saleRepository.getSaleInfo();
    }

    @Override
    public List<SalaryHistoryProjection> getAllIncomeHistoryInfo(Long id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()){
            User user = byId.get();
            if (user.getRoles().stream().anyMatch(r -> r.getRoleName().equals(RoleName.WORKER))) {
                return inputRepository.getAllInputSalaryHistory(user.getFullName());
            }
        }
        return null;
    }

    @Override
    public List<AllClientIncomeProjection> getAllClientIncome() {
        return saleRepository.allClientIncome();
    }

    @Override
    public Map<String, Double> getFinanceInfo() {
        Map<String, Double> map = new HashMap<>();
        Double wholePrice = saleRepository.sumOfIncome();
        map.put("wholePrice", wholePrice);
        Double debtPrice = saleRepository.sumOfDebt();
        map.put("debtPrice", debtPrice);
        map.put("paidPrice", Math.abs((debtPrice != null ? debtPrice : 0) - (wholePrice != null ? wholePrice : 0)));
        return map;
    }

    @Override
    public List<Outcome> getAllOutcomeHistoryInfo(Long id) {
        return outcomeRepository.findAllByUserId(id);
    }
}
