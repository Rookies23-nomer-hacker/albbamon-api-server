package com.api.domain.apply.vo;

import com.api.domain.apply.type.ApplyStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApplyVo(
        Long applyId,
        String recruitmentTitle,
        Integer recruitmentWage,
        String company,
        LocalDateTime createDate,
        ApplyStatus status
) {

}
