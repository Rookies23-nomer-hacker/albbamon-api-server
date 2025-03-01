package com.api.domain.recruitment.controller;

import com.api.domain.recruitment.dto.request.CreateRecruitmentRequestDto;
import com.api.domain.recruitment.dto.response.GetRecruitmentResponseDto;
import com.api.domain.recruitment.service.RecruitmentService;
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

@RequiredArgsConstructor
@RestController
@Tag(name = "RecruitmentMobile")
@RequestMapping("/api/mobile/recruitment")
public class RecruitmentMobileController {
    private final RecruitmentService recruitmentService;

    @Value("${server.url}")
    private String serverUrl;

    @Operation(summary = "[모바일] 내가 작성한 채용 공고 목록 보기", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/list/my")
    public ResponseEntity<SuccessResponse<?>> getMyRecruitmentList(@SessionAttribute("userid") Long userId) {
        GetRecruitmentResponseDto responseDto = recruitmentService.getMyRecruitmentList(userId);
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "[모바일] 채용 공고 작성", responses = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true)
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createRecruitment(@SessionAttribute("userid") Long userId,
                                                                @RequestPart(value = "file", required = false) MultipartFile file,
                                                                @RequestPart final CreateRecruitmentRequestDto requestDto) {
        recruitmentService.createRecruitment(userId, requestDto, file, serverUrl);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "[모바일] 채용 공고 수정", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @PostMapping("/{recruitmentId}")
    public ResponseEntity<SuccessResponse<?>> updateRecruitment(@SessionAttribute("userid") Long userId,
                                                                @PathVariable final Long recruitmentId,
                                                                @RequestBody @Valid final CreateRecruitmentRequestDto requestDto) {
        recruitmentService.updateRecruitment(userId, recruitmentId, requestDto);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "[모바일] 채용 공고 1건의 지원 이력 유무 확인", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/{recruitmentId}/apply/check")
    public ResponseEntity<Boolean> checkAlreadyAppliedRecruitment(@SessionAttribute("userid") Long userId,
                                                                  @PathVariable final Long recruitmentId) {
        return ResponseEntity.ok(recruitmentService.checkAlreadyAppliedRecruitment(userId, recruitmentId));
    }

    @Operation(summary = "[모바일] 채용 공고 지원하기", responses = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true)
    })
    @PostMapping("/{recruitmentId}/apply")
    public ResponseEntity<SuccessResponse<?>> applyRecruitment(@SessionAttribute("userid") Long userId,
                                                               @PathVariable final Long recruitmentId) {
        recruitmentService.applyRecruitment(userId, recruitmentId);
        return SuccessResponse.ok(null);
    }
}
