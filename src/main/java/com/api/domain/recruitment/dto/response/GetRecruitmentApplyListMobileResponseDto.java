package com.api.domain.recruitment.dto.response;

import com.api.domain.apply.vo.RecruitmentApplyMobileVo;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record GetRecruitmentApplyListMobileResponseDto(
        List<RecruitmentApplyMobileVo> applyList
) {
    public static GetRecruitmentApplyListMobileResponseDto of(List<RecruitmentApplyMobileVo> applyList) {
        return GetRecruitmentApplyListMobileResponseDto.builder()
                .applyList(applyList)
                .build();
    }
}
