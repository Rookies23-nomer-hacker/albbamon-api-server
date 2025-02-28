package com.api.global.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileUtil {
    @Value("${upload.recruitment.path:C:/Users/r2com/git/albbamon-api-server/src/main/webapp/uploads/recruitment/}")
    private String uploadDir;

    public String saveFile(MultipartFile file) {
        try {
            // 파일 저장 경로 확인
            String directory = uploadDir;

            // 경로가 존재하지 않으면 디렉터리를 생성
            Path path = Paths.get(directory);
            if (Files.notExists(path)) {
                Files.createDirectories(path); // 디렉터리 생성
            }

            // 파일명 설정 (현재 시간 + 원본 파일명)
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // 파일을 저장할 경로
            Path filePath = Paths.get(directory + fileName);

            // 파일 저장
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 파일 경로 리턴 (DB에 저장할 때 사용)
            return filePath.toString().replace("\\", "/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
