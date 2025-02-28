package com.api.domain.resume.controller;

import com.api.domain.resume.dto.request.CreateResumeRequestDto;
import com.api.domain.resume.request.ResumeRequestDto;
import com.api.domain.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Tag(name = "ResumeMobile")
@RequestMapping("/api/mobile/resume")
public class ResumeMobileController {

	private final ResumeService resumeService;
	private final ResumeService resumeRepository;

    @Operation(summary = "[모바일] 이력서 목록 조회", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> selectResumeMobile(@SessionAttribute("userid") Long userId) {
        Map<String,Object> response = resumeRepository.getResumeUser_id(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "[모바일] 프로필 조회", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @PostMapping("/profile")
    public ResponseEntity<Map<String, Object>> selectProfileMobile(@SessionAttribute("userid") Long userId){
        Map<String,Object> response = resumeService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "[모바일] 작성한 이력서 유무 조회", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/check")
    public ResponseEntity<?> checkResumeExists(@SessionAttribute("userid") Long userId) {
        Boolean resumeExists = resumeService.checkResumeExists(userId);
        return ResponseEntity.ok(resumeExists);
    }

    @Operation(summary = "[모바일] 이력서 삭제", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/delete")
    public ResponseEntity<String> deleteResumeMobile(@SessionAttribute("userid") Long userId){
        String response = resumeService.deleteResumeByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "[모바일] 이력서 상세 보기", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/view")
    public ResponseEntity<Map<String,Object>> viewResumeMobile(@SessionAttribute("userid") Long userId) {
        Map<String,Object> responseDto = resumeRepository.getResumeDetail(userId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "[모바일] 이력서 생성", responses = {
        @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @PostMapping("/write")
    public ResponseEntity<String> createResumeMobile(@SessionAttribute("userid") Long userId,
                                                     @RequestBody @Valid final CreateResumeRequestDto resumerequestDto,
                                                     HttpServletRequest request) {
        String portfolioName_org=resumerequestDto.portfolioName();
        String portfolioData=resumerequestDto.portfolioData();
        String resume_img_name_org = resumerequestDto.resume_img_name();
        String resume_img_data = resumerequestDto.resume_img_data();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String portfolioName ="";
        String file_url ="";
        String serverUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
        String resume_img_name = resume_img_name_org+"_"+timestamp;
        if(portfolioName_org==(null)){portfolioName=null;file_url=null;}
        else{
            String fileNameWithoutExt = portfolioName_org.substring(0, portfolioName_org.lastIndexOf("."));
            String extension = portfolioName_org.substring(portfolioName_org.lastIndexOf("."));
            portfolioName =  fileNameWithoutExt+"_"+timestamp +extension;
            file_url=serverUrl+"/upload/resume/portfolio/";
        }
        String img_url = serverUrl+"/uploads/resume/profile/";
        try {

            // 파일 저장 로직 실행
            if (portfolioData != null && portfolioName != null) {
                saveBase64ToFile(portfolioData, portfolioName,request);
                if(resume_img_data!=null) {
                    saveImgFile(resume_img_data,resume_img_name,request);
                }

                ResumeRequestDto updatedDto = new ResumeRequestDto(
                        userId,
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
                        portfolioName,
                        img_url,
                        resume_img_name,
                        resumerequestDto.resume_img_data(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
                String duplicated = resumeService.duplicated(updatedDto);

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
                        userId,
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
                        portfolioName,
                        img_url,
                        resume_img_name,
                        resumerequestDto.resume_img_data(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
                String duplicated = resumeService.duplicated(updatedDto);
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
    }

    private void saveBase64ToFile(String base64Data, String fileName, HttpServletRequest request) throws IOException {
        // Base64 데이터 디코딩
        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
        String upload_dir = request.getServletContext().getRealPath("/upload/resume/portfolio/");
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
        String upload_dir = request.getServletContext().getRealPath("/uploads/resume/profile/");
        // 파일 저장
        File file = new File(upload_dir + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(decodedBytes);
        }

        System.out.println("File saved successfully: " + file.getAbsolutePath());
    }
}
