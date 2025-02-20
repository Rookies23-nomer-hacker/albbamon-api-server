package com.api.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateUserRequestDto(
        @NotNull String email,
        @NotNull String password,
        @NotNull String name,
        @NotNull String phone,
        String company,
        String ceoNum
) {
}
