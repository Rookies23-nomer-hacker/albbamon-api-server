package com.api.domain.resume.controller;

import com.api.domain.resume.request.ResumeRequestDto;
import com.api.domain.resume.request.Resume_profileRequestDto;
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
import java.util.Base64;
import java.util.HashMap;
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

    @Operation(summary = "[모바일] 이력서 삭제", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/delete")
    public ResponseEntity<String> deleteResumeMobile(@RequestParam("resume_id") Long resumeId){
        String response = resumeService.delete(resumeId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "[모바일] 이력서 상세 보기", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/view")
    public ResponseEntity<Map<String, Object>> viewResumeMobile(@RequestParam("resume_id") Long resumeId){
        Map<String,Object> response = resumeRepository.getResume_id(resumeId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "[모바일] 이력서 생성", responses = {
        @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @PostMapping("/write")
    public ResponseEntity<String> createResumeMobile(@SessionAttribute("userid") Long userId,
                                                     @RequestBody @Valid final ResumeRequestDto resumerequestDto,
                                                     HttpServletRequest request) {
        String portfolioName=resumerequestDto.portfolioName();
        String portfolioData=resumerequestDto.portfolioData();
        String serverUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
        String file_url= serverUrl+"/uploads/resume/";
        try {
            // 파일 저장 로직 실행
            if (portfolioData != null && portfolioName != null) {
                saveBase64ToFile(portfolioData, portfolioName,request);

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

            return ResponseEntity.ok("이력서 작성 완료!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error while processing resume data."+e.getMessage());
        }
    }

    private void saveBase64ToFile(String base64Data, String fileName, HttpServletRequest request) throws IOException {
        // Base64 데이터 디코딩
        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
        String upload_dir = request.getServletContext().getRealPath("/uploads/resume/");
        // 파일 저장
        File file = new File(upload_dir + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(decodedBytes);
        }

        System.out.println("File saved successfully: " + file.getAbsolutePath());
    }
}
