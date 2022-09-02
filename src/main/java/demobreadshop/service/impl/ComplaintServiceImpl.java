package demobreadshop.service.impl;

import demobreadshop.domain.Complaint;
import demobreadshop.domain.User;
import demobreadshop.payload.ComplaintDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.repository.ComplaintRepository;
import demobreadshop.repository.UserRepository;
import demobreadshop.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public ComplaintServiceImpl(ComplaintRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Complaint> getAll() {
        return repository.findAll();
    }

    @Override
    public MyResponse create(ComplaintDto dto, MultipartFile file) {
        Optional<User> byId = userRepository.findById(dto.getUserId());
        if (byId.isPresent()) {
            User user = byId.get();

            if (file != null) {
                String s = saveFile(file);
            }
        }
        return MyResponse.USER_NOT_FOUND;
    }

    private String saveFile(MultipartFile file) {

        return null;
    }
}
