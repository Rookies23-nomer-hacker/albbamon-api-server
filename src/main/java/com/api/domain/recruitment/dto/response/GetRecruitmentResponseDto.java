package com.api.domain.recruitment.dto.response;

import com.api.domain.recruitment.vo.RecruitmentVo;
import com.api.global.common.entity.PageInfo;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record GetRecruitmentResponseDto(
        List<RecruitmentVo> recruitmentList,
        PageInfo pageInfo
) {
    public static GetRecruitmentResponseDto of(List<RecruitmentVo> recruitmentList, PageInfo pageInfo) {
        return GetRecruitmentResponseDto.builder()
                .recruitmentList(recruitmentList)
                .pageInfo(pageInfo)
                .build();
    }
}
