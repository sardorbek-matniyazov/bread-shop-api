package demobreadshop.service;

import demobreadshop.domain.Complaint;
import demobreadshop.payload.ComplaintDto;
import demobreadshop.payload.MyResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ComplaintService {
    List<Complaint> getAll();

    MyResponse create(ComplaintDto dto, MultipartFile file);

    void downloadPhoto(String fileName, HttpServletResponse response);

    Complaint get(Long id);
}
