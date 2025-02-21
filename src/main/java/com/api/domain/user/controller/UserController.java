package com.api.domain.user.controller;

import com.api.domain.user.dto.request.ChangePwRequestDto;
import com.api.domain.user.dto.request.CreateUserRequestDto;
import com.api.domain.user.dto.request.SignInRequestDto;
import com.api.domain.user.dto.response.GetUserInfoResponseDto;
import com.api.domain.user.dto.response.UserChangePwResponseDto;
import com.api.domain.user.dto.response.UserFindResponseDto;
import com.api.domain.user.service.UserService;
import com.api.global.common.entity.SuccessResponse;
import com.api.global.error.exception.EntityNotFoundException;
import com.api.global.error.exception.InvalidValueException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Tag(name = "User")
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    public static final String SESSION_NAME = "SESSIONID";

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
    public ResponseEntity<String> signIn(@RequestBody @Valid final SignInRequestDto requestDto) {

        Map<String,Object> response = new HashMap<>();
		Long userId = userService.signIn(requestDto);
		//response.put("userId", userId);
		
        return ResponseEntity.ok(String.valueOf(userId));
    }

    @Operation(summary = "로그아웃", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/sign-out")
    public ResponseEntity<SuccessResponse<?>> signOut(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        session.invalidate();
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "아이디 찾기", responses = {
    		@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/find-id")
    public ResponseEntity<List<UserFindResponseDto>> findUserId(@RequestParam("name") String name,
                                                                @RequestParam("phone") String phone) {
    	
        List<UserFindResponseDto> responseDto = userService.findUserByNameAndPhone(name, phone);
        return ResponseEntity.ok(responseDto);
    }
    
    @Operation(summary = "비밀번호 변경", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @PostMapping("/change-pw")
    public ResponseEntity<UserChangePwResponseDto> changePassword(@RequestBody ChangePwRequestDto requestDto) {
        try {
            System.out.println("📌 API 서버에서 받은 요청: " + requestDto);

            userService.changePassword(
                    requestDto.getUserId(),
                    requestDto.getPasswd(),
                    requestDto.getNewpasswd()
            );
            return ResponseEntity.ok(new UserChangePwResponseDto("비밀번호 변경 성공"));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new UserChangePwResponseDto("비밀번호 변경 실패: 사용자를 찾을 수 없습니다."));
        } catch (InvalidValueException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UserChangePwResponseDto("비밀번호 변경 실패: 현재 비밀번호가 일치하지 않습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UserChangePwResponseDto("서버 오류: " + e.getMessage()));
        }
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
