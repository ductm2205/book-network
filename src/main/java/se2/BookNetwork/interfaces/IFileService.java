package se2.BookNetwork.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    String saveFile(MultipartFile file, String userName);
}