package com.api.domain.recruitment.mapper;

import com.api.domain.recruitment.dto.response.GetRecruitmentResponseDto;
import com.api.domain.recruitment.vo.RecruitmentVo;
import com.api.global.common.entity.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecruitmentMapper {
    public GetRecruitmentResponseDto toGetRecruitmentResponseDto(List<RecruitmentVo> recruitmentList, PageInfo pageInfo) {
        return GetRecruitmentResponseDto.of(recruitmentList, pageInfo);
    }
}
