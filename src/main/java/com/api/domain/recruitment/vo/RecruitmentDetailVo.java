package com.api.domain.recruitment.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RecruitmentDetailVo(
        Long id,
        String title,
        LocalDateTime dueDate,
        LocalDateTime createDate,
        String contents,
        Integer wage,
        String userName,
        String company
) {
}
