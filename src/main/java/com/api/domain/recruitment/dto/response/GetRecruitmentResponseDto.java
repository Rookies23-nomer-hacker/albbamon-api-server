package com.api.domain.recruitment.dto.response;

import com.api.domain.recruitment.vo.RecruitmentVo;
import com.api.global.common.entity.PageInfo;
import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record GetRecruitmentResponseDto(
        List<RecruitmentVo> recruitmentList,
        PageInfo pageInfo
) {
    public static GetRecruitmentResponseDto of(Page<RecruitmentVo> recruitmentList) {
        return GetRecruitmentResponseDto.builder()
                .recruitmentList(recruitmentList.stream().toList())
                .pageInfo(PageInfo.of(recruitmentList))
                .build();
    }
}
