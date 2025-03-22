package com.api.domain.recruitment.service;

import com.api.domain.apply.entity.Apply;
import com.api.domain.apply.repository.ApplyRepository;
import com.api.domain.apply.type.ApplyStatus;
import com.api.domain.recruitment.dto.request.CreateRecruitmentRequestDto;
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
import com.api.global.common.FileType;
import com.api.global.common.entity.PageInfo;
import com.api.global.common.util.FileUtil;
import com.api.global.common.util.XorDecryptUtil;
import com.api.global.error.exception.ConflictException;
import com.api.global.error.exception.EntityNotFoundException;
import com.api.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.api.domain.apply.error.ApplyErrorCode.APPLY_ALREADY_EXISTS;
import static com.api.domain.recruitment.error.RecruitmentErrorCode.RECRUITMENT_NOT_FOUND;
import static com.api.domain.resume.error.RecruitmentErrorCode.RESUME_NOT_FOUND;
import static com.api.domain.user.error.UserErrorCode.SIGN_IN_REQUIRED;
import static com.api.domain.user.error.UserErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Transactional
@Service
public class RecruitmentService {
    @Value("${spring.datasource.encryption-key}")
    private String encryptionKey;
    private final RecruitmentRepository recruitmentRepository;
    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final ApplyRepository applyRepository;
    private final RecruitmentMapper recruitmentMapper;
    private final FileUtil fileUtil;

    public GetRecruitmentResponseDto getRecruitmentList(Pageable pageable) {
        Page<RecruitmentVo> recruitmentVos = recruitmentRepository.findAllRecruitmentVos(pageable);
        List<RecruitmentVo> recruitmentList = recruitmentVos.stream()
                .map(recruitmentVo -> new RecruitmentVo(
                        recruitmentVo.id(),
                        recruitmentVo.title(),
                        recruitmentVo.dueDate(),
                        recruitmentVo.createDate(),
                        recruitmentVo.wage(),
                        recruitmentVo.file(),
                        recruitmentVo.company(),
                        XorDecryptUtil.xorDecrypt(recruitmentVo.userName(), encryptionKey),
                        XorDecryptUtil.xorDecrypt(recruitmentVo.userEmail(), encryptionKey),
                        recruitmentVo.userCeoNum(),
                        recruitmentVo.item()
                ))
                .collect(Collectors.toList());
        return recruitmentMapper.toGetRecruitmentResponseDto(recruitmentList, PageInfo.of(recruitmentVos));
    }

    public GetRecruitmentResponseDto getMyRecruitmentList(Long userId, Pageable pageable) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        Page<RecruitmentVo> recruitmentVos = recruitmentRepository.findAllRecruitmentVosByUserId(userId, pageable);
        List<RecruitmentVo> recruitmentList = recruitmentVos.stream()
                .map(recruitmentVo -> new RecruitmentVo(
                        recruitmentVo.id(),
                        recruitmentVo.title(),
                        recruitmentVo.dueDate(),
                        recruitmentVo.createDate(),
                        recruitmentVo.wage(),
                        recruitmentVo.file(),
                        recruitmentVo.company(),
                        XorDecryptUtil.xorDecrypt(recruitmentVo.userName(), encryptionKey),
                        XorDecryptUtil.xorDecrypt(recruitmentVo.userEmail(), encryptionKey),
                        recruitmentVo.userCeoNum(),
                        recruitmentVo.item()
                ))
                .collect(Collectors.toList());
        return recruitmentMapper.toGetRecruitmentResponseDto(recruitmentList, PageInfo.of(recruitmentVos));
    }

    public RecruitmentDetailVo getRecruitment(Long recruitmentId) {
        return recruitmentRepository.findRecruitmentDetailVoById(recruitmentId).orElseThrow(() -> new EntityNotFoundException(RECRUITMENT_NOT_FOUND));
    }

    public void createRecruitment(Long userId, CreateRecruitmentRequestDto requestDto, MultipartFile file, String serverUrl) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        User user = userRepository.findUserById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        String filePath = (file != null && !file.isEmpty()) ? fileUtil.saveFile(file, FileType.RECRUITMENT, serverUrl) : null;
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

    public Long getMyRecruitmentCount(Long userId) {
        return recruitmentRepository.countByUserId(userId);
    }
}
