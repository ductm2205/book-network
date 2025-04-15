package se2.BookNetwork.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se2.BookNetwork.interfaces.IFileService;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService implements IFileService {

    @Value("${application.file.upload.output-dir}")
    private String fileUploadPath;

    @Override
    public String saveFile(@NonNull MultipartFile file, @NonNull String userName) {
        String sanitizedUserName = userName.replaceAll("[^a-zA-Z0-9]", "_");
        final String fileUploadSubPath = "users" + File.separator + sanitizedUserName;
        return uploadFile(fileUploadSubPath, file);
    }

    private String uploadFile(@NonNull String fileUploadSubPath, @NonNull MultipartFile file) {
        String finalPath = fileUploadPath + File.separator + fileUploadSubPath; // uploads/users/username1/
        File target = new File(finalPath);

        if (!target.exists()) {
            boolean isFolderCreated = target.mkdirs();

            if (!isFolderCreated) {
                log.warn("Failed to created target folder: " + target);
                return null;
            }
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());

        String savedFileName = finalPath + File.separator + System.currentTimeMillis() + "." + fileExtension;

        Path savedPath = Paths.get(savedFileName);

        try {
            Files.write(savedPath, file.getBytes());
            log.info("File saved to: " + savedFileName);
            return savedFileName;
        } catch (IOException exception) {
            log.error(exception.getMessage());
        }
        return null;
    }

    private String getFileExtension(String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()) {
            return "";
        }

        int lastDotIndex = originalFilename.lastIndexOf(".");

        if (lastDotIndex == -1) {
            return "";
        }

        return originalFilename.substring(lastDotIndex + 1);
    }

}