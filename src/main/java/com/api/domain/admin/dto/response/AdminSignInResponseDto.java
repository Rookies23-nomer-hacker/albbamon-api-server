package com.api.domain.admin.dto.response;

import lombok.Builder;

@Builder
public record AdminSignInResponseDto(
        String identity
) {
    public static AdminSignInResponseDto of(String identity) {
        return AdminSignInResponseDto.builder()
                .identity(identity)
                .build();
    }
}
