package com.api.domain.apply.controller;

import com.api.domain.apply.dto.response.GetApplyListResponseDto;
import com.api.domain.apply.service.ApplyService;
import com.api.global.common.entity.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "ApplyMobile")
@RequestMapping("/api/mobile/apply")
public class ApplyMobileController {
    private final ApplyService applyService;

    @Operation(summary = "[모바일] 나의 지원서 목록 보기", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GetApplyListResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getMyApplyList(@SessionAttribute("userid") Long userId) {
        GetApplyListResponseDto responseDto = applyService.getMyApplyList(userId);
        return SuccessResponse.ok(responseDto);
    }

    @Operation(summary = "[모바일] 나의 지원서 개수", responses = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping("/count")
    public ResponseEntity<SuccessResponse<?>> getMyApplyCount(@SessionAttribute("userid") Long userId) {
        Long applyCount = applyService.getMyApplyCount(userId);
        return SuccessResponse.ok(applyCount);
    }
}
