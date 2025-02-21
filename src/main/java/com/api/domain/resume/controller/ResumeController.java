package com.api.domain.resume.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.domain.resume.repository.ResumeRepository;
import com.api.domain.resume.request.ResumeRequestDto;
import com.api.domain.resume.request.Resume_profileRequestDto;
import com.api.domain.resume.service.ResumeService;
import com.api.domain.user.dto.request.CreateUserRequestDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController 
public class ResumeController {
	
	private final ResumeService resumeService;
	private final ResumeService resumeRepository;
	
	
	@PostMapping("/api/resume")//
	public ResponseEntity<Map<String, Object>> selectResume(@RequestBody final Resume_profileRequestDto resume_profilerequestDto){
		System.out.println("API수신");
		Map<String,Object> response = new HashMap<>();
		Long user_id = resume_profilerequestDto.user_id();
		response = resumeRepository.getresumeUser_id(user_id);
		return ResponseEntity.ok(response);
	}
	@PostMapping("/api/resume/profile")
	public ResponseEntity<Map<String, Object>> selectProfile(@RequestBody final Resume_profileRequestDto resume_profilerequestDto){
		Map<String,Object> response = new HashMap<>();
		response.put("name", resume_profilerequestDto.name());
		Long userId = resume_profilerequestDto.user_id();
		System.out.println("========="+userId);
		response = resumeService.getUserById(userId);
		return ResponseEntity.ok(response);
	}	
    @PostMapping("/api/resume/write")
    public ResponseEntity<String> createResume(@RequestBody @Valid final ResumeRequestDto resumerequestDto,
    		HttpServletRequest request) {
 
    	String portfolioName=resumerequestDto.portfolioName();
    	String portfolioData=resumerequestDto.portfolioData();
    	String serverUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
    	String file_url= serverUrl+"/uploads/";
    	System.out.println("sssssssssssssssssssssss"+resumerequestDto.user_id());
        try {
        	
        	
            // 파일 저장 로직 실행
            if (portfolioData != null && portfolioName != null) {
                saveBase64ToFile(portfolioData, portfolioName);
                
                
                ResumeRequestDto updatedDto = new ResumeRequestDto(
                		resumerequestDto.user_id(),
                		resumerequestDto.school(),
                		resumerequestDto.status(),
                		resumerequestDto.personal(),
                		resumerequestDto.work_place_region(),
                        resumerequestDto.work_place_city(),
                        resumerequestDto.industry_occupation(),
                        resumerequestDto.employmentType(),
                        resumerequestDto.working_period(),
                        resumerequestDto.working_day(),
                        resumerequestDto.introduction(),
                        resumerequestDto.portfolioData(),
                        file_url,
                        resumerequestDto.portfolioName(),
                        resumerequestDto.create_date(),
                        resumerequestDto.last_modified_date()
                );
                
          
               String duplicated = resumeService.duplicated(updatedDto);
               
               if(duplicated=="중복아님") {
                resumeService.createResume(updatedDto);
               }else {
            	   return ResponseEntity.ok("이미 이력서가 있습니다.");
               }
            } else {
            	String duplicated = resumeService.duplicated(resumerequestDto);
            	if(duplicated=="중복아님") {
            		resumeService.createResume(resumerequestDto);
            	}else {
            		return ResponseEntity.ok("이미 이력서가 있습니다.");
            	}
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
