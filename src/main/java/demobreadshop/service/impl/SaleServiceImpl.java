package demobreadshop.service.impl;

import demobreadshop.domain.*;
import demobreadshop.domain.enums.OutputType;
import demobreadshop.domain.enums.PayType;
import demobreadshop.domain.enums.SaleType;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.SaleDto;
import demobreadshop.repository.ClientRepository;
import demobreadshop.repository.OutputRepository;
import demobreadshop.repository.SaleRepository;
import demobreadshop.repository.WareHouseRepository;
import demobreadshop.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository repository;
    private final ClientRepository clientRepository;
    private final WareHouseRepository productRepository;
    private final OutputRepository outputRepository;

    @Autowired
    public SaleServiceImpl(SaleRepository repository, ClientRepository clientRepository, WareHouseRepository productRepository, OutputRepository outputRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.outputRepository = outputRepository;
    }

    @Override
    public List<Sale> getAll() {
        return repository.findAll();
    }

    @Override
    public Sale get(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public MyResponse sell(SaleDto dto) {
        final Optional<Client> byId = clientRepository.findById(dto.getClientId());
        if (byId.isPresent()) {
            final Optional<WareHouse> byId1 = productRepository.findById(dto.getProductId());
            if (byId1.isPresent()) {
                final Client client = byId.get();

                final WareHouse product = byId1.get();
                product.setAmount(product.getAmount() - dto.getAmount());

                Output output = new Output(
                        productRepository.save(product),
                        dto.getAmount(),
                        OutputType.O_SALE
                );

                Set<PayArchive> archives = new HashSet<>();
                if (dto.getCostCard() != 0.0) {
                    archives.add(new PayArchive(
                            dto.getCostCard(),
                            PayType.CARD
                    ));
                }
                if (dto.getCostCash() != 0.0) {
                    archives.add(new PayArchive(
                            dto.getCostCash(),
                            PayType.CASH
                    ));
                }

                double wholePrice = product.getPrice() * dto.getAmount();
                double debtPrice = product.getPrice() * dto.getAmount() - dto.getCostCash() - dto.getCostCard();

                repository.save(
                        new Sale(
                                outputRepository.save(output),
                                client,
                                archives,
                                wholePrice,
                                debtPrice,
                                debtPrice == 0 ? SaleType.PAYED : SaleType.DEBT
                        )
                );
                return MyResponse.SUCCESSFULLY_CREATED;
            }
            return MyResponse.PRODUCT_NOT_FOUND;
        }
        return MyResponse.CLIENT_NOT_FOUND;
    }

    @Override
    public MyResponse delete(long id) {
        return null;
    }
}
