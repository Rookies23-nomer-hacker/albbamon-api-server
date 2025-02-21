package com.api.domain.recruitment.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RecruitmentDetailVo(
        Long id,
        String title,
        LocalDateTime dueDate,
        String contents,
        Integer wage,
        String userName
) {
}
