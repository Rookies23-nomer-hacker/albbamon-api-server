package com.api.domain.recruitment.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RecruitmentVo(
        Long id,
        String title,
        LocalDateTime dueDate,
        Integer wage,
        String userName,
        String item
) {
}
