package com.api.domain.user.controller;

import com.api.domain.user.dto.request.*;
import com.api.domain.user.dto.response.GetUserInfoResponseDto;
import com.api.domain.user.dto.response.UserChangePwResponseDto;
import com.api.domain.user.dto.response.UserResponseDto;
import com.api.domain.user.service.UserService;
import com.api.domain.user.vo.UserVo;
import com.api.global.common.entity.SuccessResponse;
import com.api.global.error.exception.EntityNotFoundException;
import com.api.global.error.exception.InvalidValueException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "UserMobile")
@RequestMapping("/api/mobile/user")
public class UserMobileController {
    private final UserService userService;
    public static final String SESSION_NAME = "userid";

    @Operation(summary = "[모바일] 로그아웃", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/sign-out")
    public ResponseEntity<SuccessResponse<?>> signOut(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        session.invalidate();
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "[모바일] 자동 로그인", responses = {
        @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/autologin")
    public ResponseEntity<?> checkCache(@RequestParam("email") String email, HttpServletRequest request, HttpServletResponse response){
        
        System.out.println("server recieved email : " + email); 
        UserVo userVo = userService.autosignIn(email);
        if (userVo.id() == null) {
            // ✅ 기존 세션이 있다면 삭제하여 불필요한 세션 유지 방지
            HttpSession existingSession = request.getSession(false);
            if (existingSession != null) {
                existingSession.invalidate();
            }
        }
        // ✅ 2. 로그인 성공한 경우에만 세션 생성
        HttpSession session = request.getSession(false); // 기존 세션 확인
            if (session == null) {
            session = request.getSession(true); // 로그인 성공 시에만 새 세션 생성
            }

        session.setAttribute("userid", userVo.id()); // ✅ 세션에 사용자 ID 저장

        // ✅ 3. Set-Cookie 헤더 설정 (JSESSIONID 저장)
        String sessionId = session.getId();
        response.setHeader("Set-Cookie", "JSESSIONID=" + sessionId + "; Path=/; HttpOnly; Secure");

        // ✅ 4. 디버깅 로그 출력
        System.out.println("로그인 성공 - 세션 저장된 userid: " + session.getAttribute("userid"));
        System.out.println("세션 ID: " + sessionId);

        return ResponseEntity.ok(new UserResponseDto(userVo));

    }
    

    @Operation(summary = "[모바일] 비밀번호 변경", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @PostMapping("/change-pw")
    public ResponseEntity<UserChangePwResponseDto> changePasswordMobile(@SessionAttribute("userid") Long userId,
                                                                        @RequestBody ChangePwRequestDto requestDto) {
        try {
            userService.changePassword(
                    userId,
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

    @Operation(summary = "[모바일] 회원 탈퇴", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/withdraw")
    public ResponseEntity<SuccessResponse<?>> deleteUserMobile(@SessionAttribute("userid") Long userId) {
        userService.deleteUser(userId);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "[모바일] 회원 정보", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GetUserInfoResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getUserInfoMobile(@SessionAttribute("userid") Long userId) {
        GetUserInfoResponseDto responseDto = userService.getUserInfo(userId);
        return SuccessResponse.ok(responseDto);
    }
}
