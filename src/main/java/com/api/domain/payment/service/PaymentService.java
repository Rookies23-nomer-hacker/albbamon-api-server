package com.api.domain.payment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.payment.entity.Payment;
import com.api.domain.payment.repository.PaymentRepository;
import com.api.domain.post.mapper.PostMapper;
import com.api.domain.post.repository.PostRepository;
import com.api.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public void savePayment(String buyerName, String buyerEmail, String buyerTel, String buyerAddr, String buyerPostCode, Long userId) {
        Payment payment = new Payment();
        payment.setBuyerName(buyerName);
        payment.setBuyerEmail(buyerEmail);
        payment.setBuyerTel(buyerTel);
        payment.setBuyerAddr(buyerAddr);
        payment.setBuyerPostCode(buyerPostCode);
        payment.setUserId(userId);

        paymentRepository.save(payment);  // 결제 정보를 DB에 저장
    }
    
    public List<Long> findByUserId() {
        return paymentRepository.findByUserId();
    }
}