package com.api.domain.recruitment.service;

import com.api.domain.apply.entity.Apply;
import com.api.domain.apply.repository.ApplyRepository;
import com.api.domain.recruitment.dto.request.CreateRecruitmentRequestDto;
import com.api.domain.recruitment.dto.response.GetRecruitmentResponseDto;
import com.api.domain.recruitment.entity.Recruitment;
import com.api.domain.recruitment.mapper.RecruitmentMapper;
import com.api.domain.recruitment.repository.RecruitmentRepository;
import com.api.domain.recruitment.vo.RecruitmentVo;
import com.api.domain.resume.entity.Resume;
import com.api.domain.resume.repository.ResumeRepository;
import com.api.domain.user.entity.User;
import com.api.domain.user.repository.UserRepository;
import com.api.global.error.exception.ConflictException;
import com.api.global.error.exception.EntityNotFoundException;
import com.api.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.api.domain.apply.error.ApplyErrorCode.APPLY_ALREADY_EXISTS;
import static com.api.domain.recruitment.error.RecruitmentErrorCode.RECRUITMENT_NOT_FOUND;
import static com.api.domain.resume.error.RecruitmentErrorCode.RESUME_NOT_FOUND;
import static com.api.domain.user.error.UserErrorCode.SIGN_IN_REQUIRED;
import static com.api.domain.user.error.UserErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Transactional
@Service
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;
    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final ApplyRepository applyRepository;
    private final RecruitmentMapper recruitmentMapper;

    public GetRecruitmentResponseDto getRecruitmentList() {
        List<RecruitmentVo> recruitmentList = recruitmentRepository.findAllRecruitmentVos();
        return recruitmentMapper.toGetRecruitmentResponseDto(recruitmentList);
    }

    public GetRecruitmentResponseDto getMyRecruitmentList(Long userId) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        List<RecruitmentVo> recruitmentList = recruitmentRepository.findAllRecruitmentVosByUserId(userId);
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

    public void applyRecruitment(Long userId, Long recruitmentId) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        Recruitment recruitment = recruitmentRepository.findRecruitmentById(recruitmentId).orElseThrow(() -> new EntityNotFoundException(RECRUITMENT_NOT_FOUND));
        Resume resume = resumeRepository.findResumeByUserId(userId).orElseThrow(() -> new EntityNotFoundException(RESUME_NOT_FOUND));
        validateAlreadyApplied(recruitment.getId(), resume.getId());
        Apply apply = Apply.createApply(recruitment, resume);
        applyRepository.save(apply);
    }

    private void validateAlreadyApplied(Long recruitmentId, Long resumeId) {
        Apply apply = applyRepository.findApplyByRecruitmentIdAndResumeId(recruitmentId, resumeId);
        if(!Objects.isNull(apply)) throw new ConflictException(APPLY_ALREADY_EXISTS);
    }
}
