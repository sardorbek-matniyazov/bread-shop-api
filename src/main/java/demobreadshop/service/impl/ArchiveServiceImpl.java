package demobreadshop.service.impl;

import demobreadshop.domain.enums.OutcomeType;
import demobreadshop.domain.enums.PayType;
import demobreadshop.repository.InputRepository;
import demobreadshop.repository.OutcomeRepository;
import demobreadshop.repository.PayArchiveRepository;
import demobreadshop.repository.SaleRepository;
import demobreadshop.service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ArchiveServiceImpl implements ArchiveService {
    private final SaleRepository saleRepository;
    private final PayArchiveRepository payArchiveRepository;
    private final OutcomeRepository outcomeRepository;

    @Autowired
    public ArchiveServiceImpl(SaleRepository saleRepository, PayArchiveRepository payArchiveRepository, OutcomeRepository outcomeRepository) {
        this.saleRepository = saleRepository;
        this.payArchiveRepository = payArchiveRepository;
        this.outcomeRepository = outcomeRepository;
    }

    @Override
    public Map<String, Double> getAll() {
        Map<String, Double> map = new HashMap<>();
        map.put("WholeIncome", saleRepository.sumOfIncome());
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
}
