package com.api.domain.resume.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
public record Resume_profileRequestDto(
        @NotNull String email,
        Long user_id,
        String name,
        String phone){
////
}
