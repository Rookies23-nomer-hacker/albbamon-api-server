package com.api.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateUserRequestDto(
        @NotNull String email,
        @NotNull String password,
        @NotNull String name,
        @NotNull String phone,
        @NotNull String addr,
        @NotNull LocalDateTime bday,
        String company,
        String ceoNum
) {
}
