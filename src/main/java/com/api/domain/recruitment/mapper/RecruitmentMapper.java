package com.api.domain.recruitment.mapper;

import com.api.domain.recruitment.dto.response.GetRecruitmentResponseDto;
import com.api.domain.recruitment.vo.RecruitmentVo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecruitmentMapper {
    public GetRecruitmentResponseDto toGetRecruitmentResponseDto(List<RecruitmentVo> recruitmentList) {
        return GetRecruitmentResponseDto.of(recruitmentList);
    }
}
