package com.api.domain.payment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void updateUserPayStatus(Long userId, String item) {
    	
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        userRepository.updateItemStatus(userId, item);
    }
}