package demobreadshop.service.impl;

import demobreadshop.constants.ConstProperties;
import demobreadshop.domain.Complaint;
import demobreadshop.domain.User;
import demobreadshop.payload.ComplaintDto;
import demobreadshop.payload.MyResponse;
import demobreadshop.repository.ComplaintRepository;
import demobreadshop.repository.UserRepository;
import demobreadshop.service.ComplaintService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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

            String fileName = saveFile(file);
            if (fileName != null) {
                repository.save(
                        new Complaint(
                                dto.getDescription(),
                                user,
                                dto.getAmount(),
                                fileName
                        )
                );
            } else {
                repository.save(
                        new Complaint(
                                dto.getDescription(),
                                user,
                                dto.getAmount()
                        )
                );
            }
            user.setBalance(user.getBalance() - dto.getAmount());
            userRepository.save(user);
            return MyResponse.SUCCESSFULLY_CREATED;
        }
        return MyResponse.USER_NOT_FOUND;
    }

    @SneakyThrows
    @Override
    public void downloadPhoto(String fileName, HttpServletResponse response) {
        File file = new File(ConstProperties.FILE_PATH + fileName);
        FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
    }

    @Override
    public Complaint get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @SneakyThrows
    private String saveFile(MultipartFile file) {
        if (file != null && !file.isEmpty()){
            String string = UUID.randomUUID().toString();
            String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
            string += "." + split[split.length - 1];

            Path path = Paths.get(ConstProperties.FILE_PATH + string);
            System.out.println(ConstProperties.FILE_PATH + string);
            Files.copy(file.getInputStream(), path);

            return string;
        }
        return null;
    }
}
