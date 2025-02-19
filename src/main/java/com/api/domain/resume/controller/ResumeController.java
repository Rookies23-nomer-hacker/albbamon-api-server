package com.api.domain.resume.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.domain.resume.request.ResumeRequestDto;
import com.api.domain.resume.service.ResumeService;
import com.api.domain.user.dto.request.CreateUserRequestDto;

import jakarta.validation.Valid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@RestController 
public class ResumeController {

    @PostMapping("/api/resume/write")
    public ResponseEntity<String> createResume(@RequestBody @Valid final ResumeRequestDto resumerequestDto) {
 
    	String portfolioName=resumerequestDto.portfolioName();
    	String portfolioData=resumerequestDto.portfolioData();
        
        try {
            // 파일 저장 로직 실행
            if (portfolioData != null && portfolioName != null) {
                saveBase64ToFile(portfolioData, portfolioName);
                
                ResumeService.createResume(resumerequestDto);
            } else {
            	ResumeService.createResume(resumerequestDto);
            	return ResponseEntity.ok("파일 없음!");
            }

            return ResponseEntity.ok("파일 업로드 완료!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error while processing resume data."+e.getMessage());
        }
  
        
        
    
    }
    private void saveBase64ToFile(String base64Data, String fileName) throws IOException {
        // Base64 데이터 디코딩
        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
        String upload_dir = "src/main/resources/static/uploads/";
        // 파일 저장
        File file = new File(upload_dir + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(decodedBytes);
        }

        System.out.println("File saved successfully: " + file.getAbsolutePath());
    }
}
