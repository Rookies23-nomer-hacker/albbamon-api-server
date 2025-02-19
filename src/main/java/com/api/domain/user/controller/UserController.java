package com.api.domain.user.controller;

import com.api.domain.user.dto.request.CreateUserRequestDto;
import com.api.domain.user.dto.request.SignInRequestDto;
import com.api.domain.user.dto.response.GetUserInfoResponseDto;
import com.api.domain.user.service.UserService;
import com.api.global.common.entity.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "User")
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private static final String SESSION_NAME = "SESSIONID";

    @Operation(summary = "회원가입", responses = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true)
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createUser(@RequestBody @Valid final CreateUserRequestDto requestDto) {
        userService.createUser(requestDto);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "로그인", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @PostMapping("/sign-in")
    public ResponseEntity<SuccessResponse<?>> signIn(@RequestBody @Valid final SignInRequestDto requestDto,
                                                     HttpServletRequest httpServletRequest) {
        Long userId = userService.signIn(requestDto);
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(SESSION_NAME, userId);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "회원 탈퇴", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/withdraw")
    public ResponseEntity<SuccessResponse<?>> deleteUser(@SessionAttribute(name=SESSION_NAME) Long userId,
                                                      HttpServletRequest httpServletRequest) {
        userService.deleteUser(userId);
        HttpSession session = httpServletRequest.getSession();
        session.invalidate();
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "회원 정보", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GetUserInfoResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getUserInfo(@SessionAttribute(name=SESSION_NAME) Long userId) {
        GetUserInfoResponseDto responseDto = userService.getUserInfo(userId);
        return SuccessResponse.ok(responseDto);
    }
}
