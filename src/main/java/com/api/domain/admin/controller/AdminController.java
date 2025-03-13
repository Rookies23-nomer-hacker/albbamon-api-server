package com.api.domain.admin.controller;

import com.api.domain.admin.dto.request.AdminSignInRequestDto;
import com.api.domain.admin.dto.response.AdminSignInResponseDto;
import com.api.domain.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Admin")
@RequestMapping("/api/07060310/albba/admin")
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "관리자 로그인", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @PostMapping("/sign-in")
    public ResponseEntity<AdminSignInResponseDto> signIn(@RequestBody final AdminSignInRequestDto requestDto) {
        AdminSignInResponseDto responseDto = adminService.signIn(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
