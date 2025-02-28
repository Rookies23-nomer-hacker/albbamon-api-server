package com.api.domain.payment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.payment.request.PaymentRequest;
import com.api.domain.payment.service.PaymentService;
import com.api.domain.post.service.PostService;
import com.api.domain.user.entity.User;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

//@CrossOrigin(origins = "http://localhost:60083", allowCredentials = "true")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final HttpSession session;

    @PostMapping("/updateUserPayStatus")
    public ResponseEntity<Map<String, String>> savePayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            Long userId = paymentRequest.getUserId();

            if (userId == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(response);  // JSON 형식으로 응답 반환
            }

            // 결제 처리 후 user의 item 상태를 업데이트
            paymentService.updateUserPayStatus(userId);

            Map<String, String> response = new HashMap<>();
            response.put("message", "결제 정보가 성공적으로 저장되었습니다.");
            return ResponseEntity.ok(response);  // JSON 형식으로 응답 반환
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "결제 처리 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);  // JSON 형식으로 응답 반환
        }
    }

}