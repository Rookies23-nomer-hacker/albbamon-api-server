package com.api.domain.recruitment.dto.response;

import com.api.domain.recruitment.vo.RecruitmentVo;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record GetRecruitmentResponseDto(
        List<RecruitmentVo> recruitmentList
) {
    public static GetRecruitmentResponseDto of(List<RecruitmentVo> recruitmentList) {
        return GetRecruitmentResponseDto.builder()
                .recruitmentList(recruitmentList)
                .build();
    }
}
