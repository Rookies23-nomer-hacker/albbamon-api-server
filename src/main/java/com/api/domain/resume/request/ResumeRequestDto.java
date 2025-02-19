package com.api.domain.resume.request;

import jakarta.validation.constraints.NotNull;


public record ResumeRequestDto(
        @NotNull String school,
        @NotNull String status,
        @NotNull String personal,
        @NotNull String work_place_region,
        @NotNull String work_place_city,
        @NotNull String industry_occupation,
        @NotNull String employmentType,
        @NotNull String working_period,
        @NotNull String working_day,
        @NotNull String introduction,
        String portfolioData,
        String portfolioName) {
}
