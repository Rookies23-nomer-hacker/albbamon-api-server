package com.api.domain.apply.vo;

import com.api.domain.apply.type.ApplyStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RecruitmentApplyMobileVo(
        Long applyId,
        Long resumeId,
        String userName,
        String userEmail,
        String userPhone,
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
) {
}
