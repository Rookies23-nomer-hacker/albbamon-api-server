package com.api.domain.resume.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
public record Resume_profileRequestDto(
        @NotNull String email,
        String name,
        String phone){
////
}
