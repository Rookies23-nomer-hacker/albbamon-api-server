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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:60083")
@Tag(name = "Recruitment")
@RequestMapping("/api/recruitment")
public class RecruitmentController {
    private final RecruitmentService recruitmentService;
    private final ApplyService applyService;

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
    public ResponseEntity<SuccessResponse<?>> createRecruitment(@RequestBody @Valid final CreateRecruitmentRequestDto requestDto) {
        recruitmentService.createRecruitment(requestDto.userId(), requestDto);
        return SuccessResponse.ok(null);
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
