package com.api.domain.recruitment.controller;

import com.api.domain.apply.service.ApplyService;
import com.api.domain.apply.type.ApplyStatus;
import com.api.domain.recruitment.dto.request.CreateRecruitmentRequestDto;
import com.api.domain.recruitment.dto.request.UpdateApplyStatusRequestDto;
import com.api.domain.recruitment.dto.response.GetRecruitmentApplyListResponseDto;
import com.api.domain.recruitment.dto.response.GetRecruitmentResponseDto;
import com.api.domain.recruitment.service.RecruitmentService;
import com.api.domain.recruitment.vo.RecruitmentDetailVo;
import com.api.domain.user.dto.request.UserRequestDto;
import com.api.global.common.entity.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:60083")
@Tag(name = "Recruitment")
@RequestMapping("/api/recruitment")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;
    private final ApplyService applyService;

    @Value("${upload.recruitment.path:C:/Users/r2com/git/albbamon-api-server/src/main/webapp/uploads/recruitment/}")
    private String uploadDir;

    @Operation(summary = "채용 공고 목록 보기", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/list")
    public ResponseEntity<SuccessResponse<?>> getRecruitmentList() {
        GetRecruitmentResponseDto responseDto = recruitmentService.getRecruitmentList();
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "내가 작성한 채용 공고 목록 보기", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/list/my")
    public ResponseEntity<SuccessResponse<?>> getMyRecruitmentList(@RequestBody @Valid final UserRequestDto userRequestDto) {
        GetRecruitmentResponseDto responseDto = recruitmentService.getMyRecruitmentList(userRequestDto.userId());
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "채용 공고 1개 상세 보기", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/{recruitmentId}")
    public ResponseEntity<SuccessResponse<?>> getRecruitment(@PathVariable("recruitmentId") final Long recruitmentId) {
        RecruitmentDetailVo responseDto = recruitmentService.getRecruitment(recruitmentId);
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "채용 공고 작성", responses = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true)
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createRecruitment(
            @RequestParam(value = "file", required = false) MultipartFile file,  
            @RequestParam("userId") Long userId,
            @RequestParam("title") String title,
            @RequestParam("contents") String contents,
            @RequestParam("wage") Integer wage,
            @RequestParam("dueDate") String dueDate) {

        // 파일이 제대로 전달되는지 확인
        if (file != null && !file.isEmpty()) {
            System.out.println("파일이 정상적으로 전달되었습니다.");
        } else {
            System.out.println("파일이 전달되지 않았습니다.");
        }

        // 파일 저장 경로
        String filePath = null;
        if (file != null && !file.isEmpty()) {
            try {
                filePath = saveFile(file);  // 파일을 서버에 저장
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body(new SuccessResponse<>("파일 업로드 오류"));
            }
        } else {
            System.out.println("파일이 전달되지 않았습니다.");
        }

        // CreateRecruitmentRequestDto 객체 생성
        CreateRecruitmentRequestDto requestDto = new CreateRecruitmentRequestDto(
                userId, title, contents, LocalDateTime.parse(dueDate), wage, filePath);

        recruitmentService.createRecruitment(requestDto.userId(), requestDto);

        return SuccessResponse.ok(null);
    }

    // 파일을 서버에 저장하는 메서드
    private String saveFile(MultipartFile file) throws IOException {
        // 파일 저장 경로 확인
        String directory = uploadDir;
        
        // 경로가 존재하지 않으면 디렉터리를 생성
        Path path = Paths.get(directory);
        if (Files.notExists(path)) {
            Files.createDirectories(path); // 디렉터리 생성
        }
        System.out.println("디렉토리 경로: " + path.toString());

        // 파일명 설정 (현재 시간 + 원본 파일명)
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        
        // 파일을 저장할 경로
        Path filePath = Paths.get(directory + fileName);
        System.out.println("파일을 저장할 경로: " + filePath.toString());
        
        // 파일 저장
        Files.copy(file.getInputStream(), filePath); // 파일 저장
        
        // 파일 경로 리턴 (DB에 저장할 때 사용)
        return filePath.toString().replace("\\", "/");
    }

    @Operation(summary = "채용 공고 수정", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @PostMapping("/{recruitmentId}")
    public ResponseEntity<SuccessResponse<?>> updateRecruitment(@PathVariable final Long recruitmentId,
                                                                @RequestBody @Valid final CreateRecruitmentRequestDto requestDto) {
        recruitmentService.updateRecruitment(requestDto.userId(), recruitmentId, requestDto);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "채용 공고 1건의 지원 이력 유무 확인", responses = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true)
    })
    @GetMapping("/{recruitmentId}/apply/check")
    public ResponseEntity<Boolean> checkAlreadyAppliedRecruitment(@PathVariable final Long recruitmentId,
                                                                  @RequestBody @Valid final UserRequestDto userRequestDto) {
        return ResponseEntity.ok(recruitmentService.checkAlreadyAppliedRecruitment(userRequestDto.userId(), recruitmentId));
    }

    @Operation(summary = "채용 공고 지원하기", responses = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true)
    })
    @PostMapping("/{recruitmentId}/apply")
    public ResponseEntity<SuccessResponse<?>> applyRecruitment(@PathVariable final Long recruitmentId,
                                                               @RequestBody @Valid final UserRequestDto userRequestDto) {
        recruitmentService.applyRecruitment(userRequestDto.userId(), recruitmentId);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "채용 공고 1건의 지원서 목록 조회", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/{recruitmentId}/apply")
    public ResponseEntity<SuccessResponse<?>> getRecruitmentApplyList(@PathVariable("recruitmentId") final Long recruitmentId) {
        GetRecruitmentApplyListResponseDto responseDto = applyService.getRecruitmentApplyList(recruitmentId);
        return SuccessResponse.ok(responseDto);
    }

    // 지원서 심사
    @PostMapping("/{recruitmentId}/apply/{applyId}/status")
    public ResponseEntity<?> updateApplyStatus(
            @PathVariable("recruitmentId") Long recruitmentId,
            @PathVariable("applyId") Long applyId,
            @RequestBody UpdateApplyStatusRequestDto requestDto) {
        // status 값을 ApplyStatus enum으로 변환
        ApplyStatus status = requestDto.getStatusAsEnum();

        // 상태 업데이트 처리
        recruitmentService.updateApplyStatus(recruitmentId, applyId, status);

        return ResponseEntity.ok(new SuccessResponse<>("상태가 성공적으로 변경되었습니다."));
    }
}
