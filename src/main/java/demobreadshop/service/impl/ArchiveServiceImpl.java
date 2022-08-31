package demobreadshop.service.impl;

import demobreadshop.constants.ConstProperties;
import demobreadshop.domain.Outcome;
import demobreadshop.domain.Role;
import demobreadshop.domain.Sale;
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
import java.util.*;

@Service
@Slf4j
public class ArchiveServiceImpl implements ArchiveService {
    private final SaleRepository saleRepository;
    private final OutcomeRepository outcomeRepository;
    private final RoleRepository roleRepository;
    private final InputRepository inputRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArchiveServiceImpl(SaleRepository saleRepository, OutcomeRepository outcomeRepository, RoleRepository roleRepository, InputRepository inputRepository, ClientRepository clientRepository, UserRepository userRepository) {
        this.saleRepository = saleRepository;
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
    public Map<String, Double> outcomeStat(String start, String end) {
        Map<String, Double> stat = new HashMap<>();
        for (OutcomeType type: OutcomeType.values()) {
            if (start != null && end != null) {
                stat.put(type.name(), outcomeRepository.sumByType(type.name(), getTime(start), getTime(end)));
            }
            else stat.put(type.name(), outcomeRepository.sumByType(type.name(), new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis())));
        }
        return stat;
    }

    @Override
    public List<SaleStatistics> getAllSellerInfo(RoleName seller, String start, String end) {
        Role byRoleName = roleRepository.getByRoleName(seller);
        if (start == null && end == null) {
            return saleRepository.getAllUserInfoByRoleId(byRoleName.getId(), new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
        }
        return saleRepository.getAllUserInfoByRoleId(byRoleName.getId(), getTime(start), getTime(end));
    }

    @Override
    public List<MaterialDecreaseStat> getAllMaterialDecrease(String start, String end) {
        if (start == null && end == null) {
            return saleRepository.getAllMaterialDecrease(new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
        }
        return saleRepository.getAllMaterialDecrease(getTime(start), getTime(end));
    }

    @Override
    public List<GroupStatistics> getAllGroupStatistics(String start, String end) {
        Role byRoleName = roleRepository.getByRoleName(RoleName.WORKER);
        if (start == null && end == null) {
            return inputRepository.getAllGroupStatistics(byRoleName.getId(), new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
        }
        return inputRepository.getAllGroupStatistics(byRoleName.getId(), getTime(start), getTime(end));
    }

    @Override
    public List<InputStatistics> getAllProductStatistics(ProductType type, String start, String end) {
        if (start == null && end == null) {
            return inputRepository.getAllInputStatistics(type.name(), new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
        }
        return inputRepository.getAllInputStatistics(type.name(), getTime(start), getTime(end));
    }

    @Override
    public List<InputStatistics> getAllWarehouseStatistics(String start, String end) {
        if (start == null && end == null) {
            return inputRepository.getAllInputStatistics(ProductType.MATERIAL.name(), new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
        }
        return inputRepository.getAllInputStatistics(ProductType.MATERIAL.name(), getTime(start), getTime(end));
    }

    @Override
    public List<SellerStatistics> getAllSellerStatistics(String start, String end) {
        if (start == null && end == null) {
            return saleRepository.getAllUserStatistics(new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
        }
        return saleRepository.getAllUserStatistics(getTime(start), getTime(end));
    }

    @Override
    public List<ClientStatistics> clientStat(String start, String end) {
        if (start == null && end == null) {
            return saleRepository.getAllClientSale(new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
        }
        return saleRepository.getAllClientSale(getTime(start), getTime(start));
    }

    @Override
    public List<SaleInfoProjection> getSaleInfo(String start, String end) {
        if (start == null && end == null) {
            return saleRepository.getSaleInfo(new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
        }
        return saleRepository.getSaleInfo(getTime(start), getTime(end));
    }

    @Override
    public List<SalaryHistoryProjection> getAllIncomeHistoryInfo(Long id, String start, String end) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()){
            User user = byId.get();
            if (user.getRoles().stream().anyMatch(r -> r.getRoleName().equals(RoleName.WORKER))) {
                if (start == null && end == null) {
                    return inputRepository.getAllInputSalaryHistory(user.getFullName(), new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
                }
                return inputRepository.getAllInputSalaryHistory(user.getFullName(), getTime(start), getTime(end));
            }
        }
        return null;
    }

    @Override
    public List<AllClientIncomeProjection> getAllClientIncome(String start, String end) {
        if (start == null && end == null) {
            return saleRepository.allClientIncome(new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
        }
        return saleRepository.allClientIncome(getTime(start), getTime(end));
    }

    @Override
    public Map<String, Double> getFinanceInfo(String start, String end) {
        Map<String, Double> map = new HashMap<>();
        Double wholePrice;
        if (start == null && end == null) {
            wholePrice =  saleRepository.sumOfIncome(new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
        } else {
            wholePrice = saleRepository.sumOfIncome(getTime(start), getTime(end));
        }
        map.put("wholePrice", wholePrice);

        Double debtPrice;
        if (start == null && end == null) {
            debtPrice = saleRepository.sumOfDebtByTime(new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
        } else {
            debtPrice = saleRepository.sumOfDebtByTime(getTime(start), getTime(end));
        }
        map.put("debtPrice", debtPrice);
        map.put("paidPrice", Math.abs((debtPrice != null ? debtPrice : 0) - (wholePrice != null ? wholePrice : 0)));
        return map;
    }

    @Override
    public List<Outcome> getAllOutcomeHistoryInfo(Long id, String start, String end) {
        if (start == null && end == null) {
            return outcomeRepository.findAllByUserId(id, new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
        }
        return outcomeRepository.findAllByUserId(id, getTime(start), getTime(end));
    }

    @Override
    public List<Sale> getAllCarSellerSaleInfo(Long id, String start, String end) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            if (start == null && end == null) {
                return saleRepository.getAllSellerSales(user.getFullName(), new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
            }
            return saleRepository.getAllSellerSales(user.getFullName(), getTime(start), getTime(end));
        }
        return null;
    }

    @Override
    public List<Sale> getAllAdminSellerSaleInfo(Long id, String start, String end) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            if (start == null && end == null) {
                return saleRepository.getAllSellerSales(user.getFullName(), new Timestamp(System.currentTimeMillis() - ConstProperties.ONE_MONTH * 60 * 1000 * 60 * 60), new Timestamp(System.currentTimeMillis()));
            }
            return saleRepository.getAllSellerSales(user.getFullName(), getTime(start), getTime(end));
        }
        return null;
    }

    static Timestamp getTime(String time) {
        if (time == null) return null;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(time);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
