package demobreadshop.service.impl;

import demobreadshop.domain.*;
import demobreadshop.domain.enums.OutputType;
import demobreadshop.domain.enums.PayType;
import demobreadshop.domain.enums.SaleType;
import demobreadshop.payload.MyResponse;
import demobreadshop.payload.SaleDto;
import demobreadshop.repository.*;
import demobreadshop.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository repository;
    private final ClientRepository clientRepository;
    private final WareHouseRepository productRepository;
    private final OutputRepository outputRepository;
    private final PayArchiveRepository archiveRepository;

    @Autowired
    public SaleServiceImpl(SaleRepository repository, ClientRepository clientRepository, WareHouseRepository productRepository, OutputRepository outputRepository, PayArchiveRepository archiveRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.outputRepository = outputRepository;
        this.archiveRepository = archiveRepository;
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

                double wholePrice = product.getPrice() * dto.getAmount();
                double debtPrice = product.getPrice() * dto.getAmount() - dto.getCostCash() - dto.getCostCard();

                Sale sale = new Sale(
                        outputRepository.save(output),
                        client,
                        wholePrice,
                        debtPrice,
                        debtPrice == 0 ? SaleType.PAYED : SaleType.DEBT
                );

                repository.save(
                        sale
                );

                if (dto.getCostCard() != 0.0) {
                    archiveRepository.save(
                            new PayArchive(
                                dto.getCostCard(),
                                PayType.CARD,
                                    sale
                            )
                    );
                }
                if (dto.getCostCash() != 0.0) {
                    archiveRepository.save(
                            new PayArchive(
                                    dto.getCostCard(),
                                    PayType.CASH,
                                    sale
                            )
                    );
                }

                return MyResponse.SUCCESSFULLY_CREATED;
            }
            return MyResponse.PRODUCT_NOT_FOUND;
        }
        return MyResponse.CLIENT_NOT_FOUND;
    }

    @Transactional
    @Override
    public MyResponse delete(long id) {
        final Optional<Sale> byId = repository.findById(id);
        if (byId.isPresent()) {
            try {
                final Sale sale = byId.get();
                if (AuthServiceImpl.isNonDeletable(sale.getCreatedAt().getTime())) {
                    return MyResponse.CANT_DELETE;
                }

                WareHouse material = sale.getOutput().getMaterial();
                material.setAmount(material.getAmount() + sale.getOutput().getAmount());
                productRepository.save(material);

                repository.deleteById(id);
                return MyResponse.SUCCESSFULLY_DELETED;
            } catch (Exception e) {
                return MyResponse.CANT_DELETE;
            }
        }
        return MyResponse.SALE_NOT_FOUND;
    }

    @Override
    public List<PayArchive> getArchives(long id) {
        return archiveRepository.findAllBySaleId(id);
    }
}
