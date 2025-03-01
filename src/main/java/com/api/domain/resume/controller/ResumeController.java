package com.api.domain.resume.controller;

import com.api.domain.user.dto.request.UserRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.domain.resume.request.ResumeRequestDto;
import com.api.domain.resume.request.Resume_profileRequestDto;
import com.api.domain.resume.service.ResumeService;
import com.api.global.common.util.XorEncryptUtil;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ResumeController {
	
	private final ResumeService resumeService;
	private final ResumeService resumeRepository;
	
	@Value("${spring.datasource.encryption-key}")
  	private String encryptionKey;

	

	@PostMapping("/api/resume")
	public ResponseEntity<Map<String, Object>> selectResume(@RequestBody final Resume_profileRequestDto resume_profilerequestDto){
		System.out.println("API수신");
		Map<String,Object> response = new HashMap<>();
		Long user_id = resume_profilerequestDto.user_id();
		response = resumeRepository.getResumeUser_id(user_id);
		return ResponseEntity.ok(response);
	}
	@PostMapping("/api/resume/profile")
	public ResponseEntity<Map<String, Object>> selectProfile(@RequestBody final Resume_profileRequestDto resume_profilerequestDto){
		Map<String,Object> response = new HashMap<>();
		response.put("name", resume_profilerequestDto.name());
		Long userId = resume_profilerequestDto.user_id();
		response = resumeService.getUserById(userId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "작성한 이력서 유무 조회", responses = {
			@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	})
	@GetMapping("/api/resume/check")
	public ResponseEntity<?> checkResumeExists(@RequestBody final UserRequestDto userRequestDto) {
		Boolean resumeExists = resumeService.checkResumeExists(userRequestDto.userId());
		return ResponseEntity.ok(resumeExists);
	}
	
	@GetMapping("/api/resume/delete")
	public ResponseEntity<String> deleteResume(@RequestParam("resume_id") Long resumeId){
		String response = resumeService.delete(resumeId);
		return ResponseEntity.ok(response);
	}	
	
	@GetMapping("/api/resume/view")
	public ResponseEntity<Map<String, Object>> viewResume(@RequestParam("resume_id") Long resumeId){
		Map<String,Object> response = new HashMap<>();
		response = resumeRepository.getResume_id(resumeId);
		Long userId = (Long) response.get("user_id");
		System.out.println("img"+response.get("resume_imgurl"));
		System.out.println("img"+response.get("resume_img_name"));
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/api/resume/download")
    public void download(@RequestParam("fileName") String filename, HttpServletRequest request, HttpServletResponse response) {
        // 실제 파일 경로 지정
        String filePath = "/home/api_root/download/apache-tomcat-10.1.36/webapps/ROOT/upload/resume/portfolio/"+ filename;
        File downloadFile = new File(filePath);

        // 파일 존재 여부 확인
        if (!downloadFile.exists()) {
            System.out.println("파일이 존재하지 않습니다: " + filePath);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 파일 스트림을 열어서 응답으로 전송
        try (FileInputStream fis = new FileInputStream(downloadFile);
             ServletOutputStream os = response.getOutputStream()) {

            // 응답 헤더 설정
            response.setContentType("application/octet-stream");
            response.setContentLength((int) downloadFile.length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + cleanFilename(downloadFile.getName()) + "\"");

            // 파일 데이터를 클라이언트로 전송
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush(); // 버퍼 비우기

            System.out.println("파일 다운로드 성공: " + filename);
        } catch (IOException e) {
            System.err.println("파일 다운로드 중 오류 발생: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/resume/write")
    public ResponseEntity<String> createResume(@RequestBody @Valid final ResumeRequestDto resumerequestDto,
											   HttpServletRequest request) {
 
    	String portfolioName_org=resumerequestDto.portfolioName();
    	String portfolioData=resumerequestDto.portfolioData();
    	String resume_img_name_org = resumerequestDto.resume_img_name();
    	String resume_img_data = resumerequestDto.resume_img_data();
    	String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    	String portfolioName ="";
    	String file_url ="";
    	String serverUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
    	
    	
    	String img_name = resume_img_name_org.substring(0, resume_img_name_org.lastIndexOf("."));
    	String img_extension = resume_img_name_org.substring(resume_img_name_org.lastIndexOf("."));
    	String resume_img_name = img_name+"_"+timestamp+img_extension;
    	if(portfolioName_org==(null)){
    		portfolioName=null;
    		file_url=null;
    	}
    	else{
    		String fileNameWithoutExt = portfolioName_org.substring(0, portfolioName_org.lastIndexOf("."));
    		String extension = portfolioName_org.substring(portfolioName_org.lastIndexOf("."));
    		portfolioName =  fileNameWithoutExt+"_"+timestamp +extension;
    		file_url=serverUrl+"/upload/resume/portfolio/";}
    	String img_url = serverUrl+"/upload/resume/profile/";
        try {

            // 파일 저장 로직 실행
            if (portfolioData != null && portfolioName != null) {
                saveBase64ToFile(portfolioData, portfolioName,request);
                if(resume_img_data!=null) {
                	saveImgFile(resume_img_data,resume_img_name,request);
                }
                
                ResumeRequestDto updatedDto = new ResumeRequestDto(
                		resumerequestDto.user_id(),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.school(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.status(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.personal(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.work_place_region(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.work_place_city(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.industry_occupation(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.employmentType(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.working_period(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.working_day(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.introduction(), encryptionKey),
                	    resumerequestDto.portfolioData(),
                	    file_url,
                	    portfolioName,
                	    img_url, 
                	    resume_img_name,
                	    resumerequestDto.resume_img_data(),
                	    resumerequestDto.create_date(),
                	    resumerequestDto.last_modified_date()
                );
               String duplicated = resumeService.duplicated(updatedDto.user_id());
       
               if(duplicated=="중복아님") {
            	
                resumeService.createResume(updatedDto);
               }else {
            	   return ResponseEntity.ok("이미 이력서가 있습니다.");
               }
            } else {
            	
            	if(resume_img_data!=null) {
                	saveImgFile(resume_img_data,resume_img_name,request);
                }
            	ResumeRequestDto updatedDto = new ResumeRequestDto(
            			resumerequestDto.user_id(),
            			XorEncryptUtil.xorEncrypt(resumerequestDto.school(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.status(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.personal(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.work_place_region(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.work_place_city(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.industry_occupation(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.employmentType(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.working_period(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.working_day(), encryptionKey),
                		XorEncryptUtil.xorEncrypt(resumerequestDto.introduction(), encryptionKey),
                	    resumerequestDto.portfolioData(),
                	    file_url,
                	    portfolioName,
                	    img_url, 
                	    resume_img_name,
                	    resumerequestDto.resume_img_data(),
                	    resumerequestDto.create_date(),
                	    resumerequestDto.last_modified_date()
                );
            	String duplicated = resumeService.duplicated(updatedDto.user_id());
            	if(duplicated=="중복아님") {
            		resumeService.createResume(updatedDto);
            	}else {
            		return ResponseEntity.ok("이미 이력서가 있습니다.");
            	}
            }

            return ResponseEntity.ok("이력서 작성 완료!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error while processing resume data."+e.getMessage());
        }

    }//
    private void saveBase64ToFile(String base64Data, String fileName, HttpServletRequest request) throws IOException {
        // Base64 데이터 디코딩
        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
        String serverUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();

        String upload_dir = "/home/api_root/download/apache-tomcat-10.1.36/webapps/ROOT/upload/resume/portfolio/";

        // 파일 저장
        File file = new File(upload_dir + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(decodedBytes);
        }

        System.out.println("File saved successfully: " + file.getAbsolutePath());
    }
    
    private void saveImgFile(String base64Data, String fileName, HttpServletRequest request) throws IOException {
        // Base64 데이터 디코딩
        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
        String serverUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();

        String upload_dir = "/home/api_root/download/apache-tomcat-10.1.36/webapps/ROOT/upload/resume/profile/";

        // 파일 저장
        File file = new File(upload_dir + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(decodedBytes);
        }

        System.out.println("File saved successfully: " + file.getAbsolutePath());
    }
    
    private String cleanFilename(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex != -1) {
            return filename.substring(0, dotIndex) + filename.substring(dotIndex).split("_")[0];
        }
        return filename; // 확장자가 없으면 원본 유지
    }
}
