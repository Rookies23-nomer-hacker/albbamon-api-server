package com.api.domain.apply.controller;

import com.api.domain.apply.dto.response.GetApplyListResponseDto;
import com.api.domain.apply.service.ApplyService;
import com.api.domain.recruitment.dto.response.GetRecruitmentResponseDto;
import com.api.global.common.entity.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import static com.api.domain.user.controller.UserController.SESSION_NAME;

@RequiredArgsConstructor
@RestController
@Tag(name = "Apply")
@RequestMapping("/api/apply")
public class ApplyController {
    private final ApplyService applyService;

    @Operation(summary = "나의 지원서 목록 보기", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/list/my")
    public ResponseEntity<SuccessResponse<?>> getMyApplyList(@SessionAttribute(name=SESSION_NAME) Long userId) {
        GetApplyListResponseDto responseDto = applyService.getMyApplyList(userId);
        return SuccessResponse.ok(responseDto);
    }
}
