package com.api.domain.recruitment.dto.response;

import com.api.domain.apply.vo.RecruitmentApplyVo;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record GetRecruitmentApplyListResponseDto(
        List<RecruitmentApplyVo> applyList
) {
    public static GetRecruitmentApplyListResponseDto of(List<RecruitmentApplyVo> applyList) {
        return GetRecruitmentApplyListResponseDto.builder()
                .applyList(applyList)
                .build();
    }
}
