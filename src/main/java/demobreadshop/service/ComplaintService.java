package demobreadshop.service;

import demobreadshop.domain.Complaint;
import demobreadshop.payload.ComplaintDto;
import demobreadshop.payload.MyResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ComplaintService {
    List<Complaint> getAll();

    MyResponse create(ComplaintDto dto, MultipartFile file);
}
