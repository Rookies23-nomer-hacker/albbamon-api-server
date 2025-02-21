package com.api.domain.apply.dto.response;

import com.api.domain.apply.vo.ApplyVo;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record GetApplyListResponseDto(
        List<ApplyVo> applyList
) {
    public static GetApplyListResponseDto of(List<ApplyVo> applyList) {
        return GetApplyListResponseDto.builder()
                .applyList(applyList)
                .build();
    }
}
