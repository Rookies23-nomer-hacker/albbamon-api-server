package com.api.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;

public record SignInRequestDto(
        @NotNull String email,
        @NotNull Long userId,
        @NotNull String password,
        @NotNull String ceoNum
) {
}
