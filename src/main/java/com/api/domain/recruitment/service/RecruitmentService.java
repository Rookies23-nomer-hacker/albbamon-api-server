package com.api.domain.recruitment.service;

import com.api.domain.apply.entity.Apply;
import com.api.domain.apply.repository.ApplyRepository;
import com.api.domain.apply.type.ApplyStatus;
import com.api.domain.recruitment.dto.request.CreateRecruitmentRequestDto;
import com.api.domain.recruitment.dto.request.UpdateApplyStatusRequestDto;
import com.api.domain.recruitment.dto.response.GetRecruitmentResponseDto;
import com.api.domain.recruitment.entity.Recruitment;
import com.api.domain.recruitment.mapper.RecruitmentMapper;
import com.api.domain.recruitment.repository.RecruitmentRepository;
import com.api.domain.recruitment.vo.RecruitmentDetailVo;
import com.api.domain.recruitment.vo.RecruitmentVo;
import com.api.domain.resume.entity.Resume;
import com.api.domain.resume.repository.ResumeRepository;
import com.api.domain.user.entity.User;
import com.api.domain.user.repository.UserRepository;
import com.api.global.common.entity.SuccessResponse;
import com.api.global.common.util.FileUtil;
import com.api.global.error.exception.ConflictException;
import com.api.global.error.exception.EntityNotFoundException;
import com.api.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private static final String RECRUITMENT_FILE_DIR = "/home/api_root/download/apache-tomcat-10.1.36/webapps/ROOT/upload/recruitment/";
    private final RecruitmentRepository recruitmentRepository;
    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final ApplyRepository applyRepository;
    private final RecruitmentMapper recruitmentMapper;
    private final FileUtil fileUtil;

    public GetRecruitmentResponseDto getRecruitmentList() {
        List<RecruitmentVo> recruitmentList = recruitmentRepository.findAllRecruitmentVos();
        return recruitmentMapper.toGetRecruitmentResponseDto(recruitmentList);
    }

    public GetRecruitmentResponseDto getMyRecruitmentList(Long userId) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        List<RecruitmentVo> recruitmentList = recruitmentRepository.findAllRecruitmentVosByUserId(userId);
        return recruitmentMapper.toGetRecruitmentResponseDto(recruitmentList);
    }

    public RecruitmentDetailVo getRecruitment(Long recruitmentId) {
        return recruitmentRepository.findRecruitmentDetailVoById(recruitmentId).orElseThrow(() -> new EntityNotFoundException(RECRUITMENT_NOT_FOUND));
    }

    public void createRecruitment(Long userId, CreateRecruitmentRequestDto requestDto, MultipartFile file) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        User user = userRepository.findUserById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        String filePath = (file != null && !file.isEmpty()) ? fileUtil.saveFile(file, RECRUITMENT_FILE_DIR) : null;
        Recruitment recruitment = Recruitment.createRecruitment(user, requestDto, filePath);
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

    public void updateApplyStatus(Long applyId, ApplyStatus status) {
        Apply apply = applyRepository.findById(applyId).orElseThrow(() -> new IllegalArgumentException("지원서를 찾을 수 없습니다."));
        apply.setStatus(status);
        applyRepository.save(apply); // 상태 저장
    }

    public Boolean checkAlreadyAppliedRecruitment(Long userId, Long recruitmentId) {
        Resume resume = resumeRepository.findResumeByUserId(userId).orElseThrow(() -> new EntityNotFoundException(RESUME_NOT_FOUND));
        return applyRepository.existsByResumeIdAndRecruitmentId(resume.getId(), recruitmentId);
    }
}
