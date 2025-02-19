package com.api.domain.recruitment.service;

import com.api.domain.recruitment.dto.request.CreateRecruitmentRequestDto;
import com.api.domain.recruitment.entity.Recruitment;
import com.api.domain.recruitment.repository.RecruitmentRepository;
import com.api.domain.user.entity.User;
import com.api.domain.user.repository.UserRepository;
import com.api.global.error.exception.EntityNotFoundException;
import com.api.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.api.domain.user.error.UserErrorCode.SIGN_IN_REQUIRED;
import static com.api.domain.user.error.UserErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Transactional
@Service
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;
    private final UserRepository userRepository;

    public void createRecruitment(Long userId, CreateRecruitmentRequestDto requestDto) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        User user = userRepository.findUserById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        Recruitment recruitment = Recruitment.createRecruitment(user, requestDto);
        recruitmentRepository.save(recruitment);
    }
}
