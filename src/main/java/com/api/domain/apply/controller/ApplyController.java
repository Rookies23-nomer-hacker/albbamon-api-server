package com.api.domain.apply.controller;

import com.api.domain.apply.dto.response.GetApplyListResponseDto;
import com.api.domain.apply.service.ApplyService;
import com.api.domain.user.dto.request.UserRequestDto;
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
@Tag(name = "Apply")
@RequestMapping("/api/apply")
public class ApplyController {
    private final ApplyService applyService;

    @Operation(summary = "나의 지원서 목록 보기", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GetApplyListResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getMyApplyList(@RequestBody final UserRequestDto userRequestDto) {
        GetApplyListResponseDto responseDto = applyService.getMyApplyList(userRequestDto.userId());
        return SuccessResponse.ok(responseDto);
    }
}
