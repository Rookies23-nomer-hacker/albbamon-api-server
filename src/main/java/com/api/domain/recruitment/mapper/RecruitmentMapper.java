package com.api.domain.recruitment.mapper;

import com.api.domain.recruitment.dto.response.GetRecruitmentResponseDto;
import com.api.domain.recruitment.vo.RecruitmentVo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class RecruitmentMapper {
    public GetRecruitmentResponseDto toGetRecruitmentResponseDto(Page<RecruitmentVo> recruitmentList) {
        return GetRecruitmentResponseDto.of(recruitmentList);
    }
}
