package com.api.domain.payment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.payment.entity.Payment;
import com.api.domain.post.mapper.PostMapper;
import com.api.domain.post.repository.PostRepository;
import com.api.domain.user.entity.User;
import com.api.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class PaymentService {

    private final UserRepository userRepository;

    public void updateUserPayStatus(Long userId) {
        // userId에 해당하는 사용자 정보를 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // DB에 저장하여 업데이트를 반영
        userRepository.updateItemStatus(userId, "Y");
    }
}