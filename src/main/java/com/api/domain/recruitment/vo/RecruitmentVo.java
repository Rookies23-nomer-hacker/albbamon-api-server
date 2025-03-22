package com.api.domain.recruitment.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RecruitmentVo(
        Long id,
        String title,
        LocalDateTime dueDate,
        LocalDateTime createDate,
        Integer wage,
        String file,
        String company,
        String userName,
        String userEmail,
        String userCeoNum,
        String item
) {
}
