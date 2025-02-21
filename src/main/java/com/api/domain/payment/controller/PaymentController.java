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
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.payment.request.PaymentRequest;
import com.api.domain.payment.service.PaymentService;
import com.api.domain.post.service.PostService;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;


    @CrossOrigin(origins = "http://localhost:60083")
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> savepaymentRequest(@RequestBody PaymentRequest paymentRequest) {
    	Map<String, String> response = new HashMap<>();
        try {
            String buyerName = paymentRequest.getBuyerName();
            String buyerEmail = paymentRequest.getBuyerEmail();
            String buyerTel = paymentRequest.getBuyerTel();
            String buyerAddr = paymentRequest.getBuyerAddr();
            String buyerPostcode = paymentRequest.getBuyerPostcode();
            Long userId = paymentRequest.getUserId();

            // 결제 정보 저장
            paymentService.savePayment(buyerName, buyerEmail, buyerTel, buyerAddr, buyerPostcode, userId);
            
            response.put("message", "결제 정보가 성공적으로 저장되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "결제 정보 저장에 실패했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // Recruitment 공고 게시판 userId와 join
    @GetMapping("/findUserId")
    public ResponseEntity<List<Long>> getfindByUserId() {
        List<Long> recruitmentIds = paymentService.findByUserId();
        return ResponseEntity.ok(recruitmentIds);
    }
    
}