package com.api.domain.recruitment.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateRecruitmentRequestDto(
        @NotNull Long userId,
        @NotNull String title,
        @NotNull String contents,
        @NotNull LocalDateTime dueDate,
        @NotNull Integer wage
) {
}
