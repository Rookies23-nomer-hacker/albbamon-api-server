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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:60083")
@Tag(name = "RecruitmentMobile")
@RequestMapping("/api/mobile/recruitment")
public class RecruitmentMobileController {
    private final RecruitmentService recruitmentService;


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
                                                                @RequestBody @Valid final CreateRecruitmentRequestDto requestDto) {
        recruitmentService.createRecruitment(userId, requestDto);
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
