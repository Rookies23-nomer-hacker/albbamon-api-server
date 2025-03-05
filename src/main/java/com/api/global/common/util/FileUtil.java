package com.api.global.common.util;

import com.api.global.common.FileType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FileUtil {
    @Value("${file.dir}")
    private String uploadDir;

    public String saveFile(MultipartFile multipartFile, FileType fileType, String serverUrl) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            String originalFileName = multipartFile.getOriginalFilename().substring(0, multipartFile.getOriginalFilename().lastIndexOf("."));
            String extension = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
            String fileName = originalFileName + "_" + timestamp + extension;

            byte[] decodedBytes = multipartFile.getBytes();
            String upload_dir = uploadDir + fileType.getPath();

            File file = new File(upload_dir + fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(decodedBytes);
            }

            return serverUrl + "/upload/" + fileType.getPath() + fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
