package com.api.domain.apply.service;

import com.api.domain.apply.dto.response.GetApplyListResponseDto;
import com.api.domain.apply.repository.ApplyRepository;
import com.api.domain.apply.vo.ApplyVo;
import com.api.domain.apply.vo.RecruitmentApplyVo;
import com.api.domain.recruitment.dto.response.GetRecruitmentApplyListResponseDto;
import com.api.domain.resume.entity.Resume;
import com.api.domain.resume.repository.ResumeRepository;
import com.api.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import com.api.global.common.util.XorDecryptUtil;

import static com.api.domain.user.error.UserErrorCode.SIGN_IN_REQUIRED;

@RequiredArgsConstructor
@Service
public class ApplyService {
    private final ApplyRepository applyRepository;
	private final ResumeRepository resumeRepository;
    
    @Value("${spring.datasource.encryption-key}")
  	private String encryptionKey;

    public GetApplyListResponseDto getMyApplyList(Long userId) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        List<ApplyVo> applyVoList = applyRepository.findApplyVoByUserId(userId);

        return GetApplyListResponseDto.of(applyVoList);
    }

    public GetRecruitmentApplyListResponseDto getRecruitmentApplyList(@PathVariable("recruitmentId") Long recruitmentId) {
        List<RecruitmentApplyVo> recruitmentApplyVoList = applyRepository.findRecruitmentApplyVoByRecruitmentId(recruitmentId);
        List<RecruitmentApplyVo> reApplyList = recruitmentApplyVoList.stream()
                .map(apply -> new RecruitmentApplyVo(
            		apply.applyId(),
					apply.resumeId(),
            		XorDecryptUtil.xorDecrypt(apply.userName(), encryptionKey),
            		XorDecryptUtil.xorDecrypt(apply.school(), encryptionKey),
            		XorDecryptUtil.xorDecrypt(apply.status(), encryptionKey),
            		XorDecryptUtil.xorDecrypt(apply.personal(), encryptionKey),
            		XorDecryptUtil.xorDecrypt(apply.workPlaceRegion(), encryptionKey),
            		XorDecryptUtil.xorDecrypt(apply.workPlaceCity(), encryptionKey),
            		XorDecryptUtil.xorDecrypt(apply.industryOccupation(), encryptionKey),
            		XorDecryptUtil.xorDecrypt(apply.employmentType(), encryptionKey),
            		XorDecryptUtil.xorDecrypt(apply.workingPeriod(), encryptionKey),
            		XorDecryptUtil.xorDecrypt(apply.workingDay(), encryptionKey),
            		XorDecryptUtil.xorDecrypt(apply.introduction(), encryptionKey),
            		apply.portfoliourl(),
            		apply.portfolioname(),
            		apply.resume_imgurl(),
            		apply.resume_imgname(),
            		apply.createDate(),
            		apply.applyStatus()
        		))
            .toList();
        return GetRecruitmentApplyListResponseDto.of(reApplyList);
    }

    public Long getMyApplyCount(Long userId) {
		if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
		Resume resume = resumeRepository.findResumeByUserId(userId).orElse(null);
		if(resume == null) return 0L;
		return applyRepository.countByResumeId(resume.getId());
    }
}


/*
 applyId,
        String userName,
        String school,
        String status,
        String personal,
        String workPlaceRegion,
        String workPlaceCity,
        String industryOccupation,
        String employmentType,
        String workingPeriod,
        String workingDay,
        String introduction,
        String portfoliourl,
        String portfolioname,
        String resume_imgurl,
        String resume_imgname,
        LocalDateTime createDate,
        ApplyStatus applyStatus
*/