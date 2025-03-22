package com.api.domain.recruitment.dto.request;

import jakarta.validation.constraints.NotNull;

public record GetRecruitmentApplyListMobileRequestDto(
        @NotNull Long recruitmentId
) {
}
