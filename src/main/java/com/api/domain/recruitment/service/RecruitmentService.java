package com.api.domain.recruitment.service;

import com.api.domain.recruitment.dto.request.CreateRecruitmentRequestDto;
import com.api.domain.recruitment.dto.response.GetRecruitmentResponseDto;
import com.api.domain.recruitment.entity.Recruitment;
import com.api.domain.recruitment.mapper.RecruitmentMapper;
import com.api.domain.recruitment.repository.RecruitmentRepository;
import com.api.domain.recruitment.vo.RecruitmentVo;
import com.api.domain.user.entity.User;
import com.api.domain.user.repository.UserRepository;
import com.api.global.error.exception.EntityNotFoundException;
import com.api.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.api.domain.recruitment.error.RecruitmentErrorCode.RECRUITMENT_NOT_FOUND;
import static com.api.domain.user.error.UserErrorCode.SIGN_IN_REQUIRED;
import static com.api.domain.user.error.UserErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Transactional
@Service
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;
    private final UserRepository userRepository;
    private final RecruitmentMapper recruitmentMapper;

    public GetRecruitmentResponseDto getRecruitmentList() {
        List<RecruitmentVo> recruitmentList = recruitmentRepository.findAllRecruitmentVos();
        return recruitmentMapper.toGetRecruitmentResponseDto(recruitmentList);
    }

    public void createRecruitment(Long userId, CreateRecruitmentRequestDto requestDto) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        User user = userRepository.findUserById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        Recruitment recruitment = Recruitment.createRecruitment(user, requestDto);
        recruitmentRepository.save(recruitment);
    }

    public void updateRecruitment(Long userId, Long recruitmentId, CreateRecruitmentRequestDto requestDto) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        Recruitment recruitment = recruitmentRepository.findRecruitmentById(recruitmentId).orElseThrow(() -> new EntityNotFoundException(RECRUITMENT_NOT_FOUND));
        recruitment.updateRecruitment(requestDto);
    }
}
