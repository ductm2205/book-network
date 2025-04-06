package se2.BookNetwork.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {

    public static byte[] readFileFromLocation(String bookCoverUrl) {
        if (StringUtils.isBlank(bookCoverUrl)) {
            return new byte[0];
        }

        try {
            Path filePath = new File(bookCoverUrl).toPath();
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.error("Failed to get the file at location: {} ", bookCoverUrl, e.getMessage());
        }
        return new byte[0];
    }

}