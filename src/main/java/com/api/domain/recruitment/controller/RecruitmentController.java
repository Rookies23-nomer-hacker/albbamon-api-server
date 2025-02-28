package com.api.domain.recruitment.controller;

import com.api.domain.apply.service.ApplyService;
import com.api.domain.recruitment.dto.request.CreateRecruitmentRequestDto;
import com.api.domain.recruitment.dto.request.UpdateApplyStatusRequestDto;
import com.api.domain.recruitment.dto.response.GetRecruitmentApplyListResponseDto;
import com.api.domain.recruitment.dto.response.GetRecruitmentResponseDto;
import com.api.domain.recruitment.service.RecruitmentService;
import com.api.domain.recruitment.vo.RecruitmentDetailVo;
import com.api.domain.user.dto.request.UserRequestDto;
import com.api.global.common.entity.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:60083")
@Tag(name = "Recruitment")
@RequestMapping("/api/recruitment")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;
    private final ApplyService applyService;

    @Operation(summary = "채용 공고 목록 보기", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GetRecruitmentResponseDto.class)))
    })
    @GetMapping("/list")
    public ResponseEntity<SuccessResponse<?>> getRecruitmentList() {
        GetRecruitmentResponseDto responseDto = recruitmentService.getRecruitmentList();
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "내가 작성한 채용 공고 목록 보기", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GetRecruitmentResponseDto.class)))
    })
    @GetMapping("/list/my")
    public ResponseEntity<SuccessResponse<?>> getMyRecruitmentList(@RequestBody @Valid final UserRequestDto userRequestDto) {
        GetRecruitmentResponseDto responseDto = recruitmentService.getMyRecruitmentList(userRequestDto.userId());
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "채용 공고 1개 상세 보기", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = RecruitmentDetailVo.class)))
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
    public ResponseEntity<SuccessResponse<?>> createRecruitment(@RequestPart(value = "file", required = false) MultipartFile file,
                                                                @RequestPart final CreateRecruitmentRequestDto requestDto) {
        if (requestDto == null) {
            throw new IllegalArgumentException("요청 데이터가 없습니다.");
        }
        recruitmentService.createRecruitment(requestDto.userId(), requestDto, file);
        return SuccessResponse.created(null);
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
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Boolean.class)))
    })
    @GetMapping("/{recruitmentId}/apply/check")
    public ResponseEntity<Boolean> checkAlreadyAppliedRecruitment(@PathVariable("recruitmentId") final Long recruitmentId,
                                                                  @RequestBody @Valid final UserRequestDto userRequestDto) {
        return ResponseEntity.ok(recruitmentService.checkAlreadyAppliedRecruitment(userRequestDto.userId(), recruitmentId));
    }

    @Operation(summary = "채용 공고 지원하기", responses = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true)
    })
    @PostMapping("/{recruitmentId}/apply")
    public ResponseEntity<SuccessResponse<?>> applyRecruitment(@PathVariable("recruitmentId") final Long recruitmentId,
                                                               @RequestBody @Valid final UserRequestDto userRequestDto) {
        recruitmentService.applyRecruitment(userRequestDto.userId(), recruitmentId);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "채용 공고 1건의 지원서 목록 조회", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GetRecruitmentApplyListResponseDto.class)))
    })
    @GetMapping("/{recruitmentId}/apply")
    public ResponseEntity<SuccessResponse<?>> getRecruitmentApplyList(@PathVariable("recruitmentId") final Long recruitmentId) {
        GetRecruitmentApplyListResponseDto responseDto = applyService.getRecruitmentApplyList(recruitmentId);
        return SuccessResponse.ok(responseDto);
    }


    @Operation(summary = "채용 공고 1건의 지원서 합불 결과 변경", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/{recruitmentId}/apply/{applyId}/status")
    public ResponseEntity<?> updateApplyStatus(@PathVariable("recruitmentId") Long recruitmentId,
                                               @PathVariable("applyId") Long applyId,
                                               @RequestBody UpdateApplyStatusRequestDto requestDto) {
        recruitmentService.updateApplyStatus(applyId, requestDto.getStatusAsEnum());
        return ResponseEntity.ok(new SuccessResponse<>("상태가 성공적으로 변경되었습니다."));
    }
}
