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
import java.io.FileNotFoundException;
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
                                fileName,
                                file.getContentType()
                        )
                );
            } else {
                repository.save(
                        new Complaint(
                                dto.getDescription(),
                                user,
                                dto.getAmount(),
                                "default.jpg"
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
        try {
            File file = new File(ConstProperties.FILE_PATH + fileName);
            Optional<Complaint> byFileName = repository.findByFileName(fileName);
            if (byFileName.isPresent()) {
                Complaint complaint = byFileName.get();
                if (complaint.getContentType() != null) {
                    response.setContentType(complaint.getContentType());
                }
            }
            FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
        } catch (FileNotFoundException e) {
            File file = new File(ConstProperties.FILE_PATH + "default.jpeg");
            response.setContentType("image/jpg");
            FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
        }
    }

    @Override
    public Complaint get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public String saveImage(MultipartFile file) {
        return saveFile(file);
    }

    @Override
    public MyResponse delete(Long id) {
        Optional<Complaint> byId = repository.findById(id);
        if (byId.isPresent()) {

            Complaint complaint = byId.get();
            if (AuthServiceImpl.isNonDeletable(complaint.getCreatedAt().getTime())) {
                return MyResponse.CANT_DELETE;
            }

            Optional<User> byFullName = userRepository.findById(complaint.getId());
            if (byFullName.isPresent()) {
                User user = byFullName.get();
                user.setBalance(user.getBalance() + complaint.getAmount());
                userRepository.save(user);
            }

            repository.delete(complaint);

            File file = new File(ConstProperties.FILE_PATH + complaint.getFileName());
            if (file.delete()) {
                System.out.println("Deleted file");
            }
            return MyResponse.SUCCESSFULLY_DELETED;
        }
        return MyResponse.COMPLAINT_NOT_FOUND;
    }

    @SneakyThrows
    private String saveFile(MultipartFile file) {
        if (file != null && !file.isEmpty()){
            String string = UUID.randomUUID().toString();
            String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
            string += "." + split[split.length - 1];

            Path path = Paths.get(ConstProperties.FILE_PATH + string);
            Files.copy(file.getInputStream(), path);

            return string;
        }
        return null;
    }
}
