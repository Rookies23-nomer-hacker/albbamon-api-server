package com.api.domain.admin.dto.request;

import jakarta.validation.constraints.NotNull;

public record AdminSignUpRequestDto(
        @NotNull String identity,
        @NotNull String password
) {
}
