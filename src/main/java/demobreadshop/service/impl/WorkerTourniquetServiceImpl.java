package demobreadshop.service.impl;

import demobreadshop.domain.WorkerTourniquet;
import demobreadshop.repository.WorkerTourniquetRepository;
import demobreadshop.service.WorkerTourniquetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class WorkerTourniquetServiceImpl implements WorkerTourniquetService {
    private final WorkerTourniquetRepository repository;

    @Autowired
    public WorkerTourniquetServiceImpl(WorkerTourniquetRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<WorkerTourniquet> getAllWorkerTourniquet() {
        return repository.findAllByOrderByIdDesc();
    }
}
