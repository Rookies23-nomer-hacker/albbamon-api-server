package com.api.domain.user.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public record UserVo(
        Long id,
        String name,
        String email,
        String phone,
        String ceoNum,
        String company,
        String profileImg,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime lastModifiedDate
) {
}
