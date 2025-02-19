package com.api.domain.user.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserVo(
        Long id,
        String name,
        String email,
        String phone,
        String addr,
        LocalDateTime bday,
        String ceoNum,
        String company,
        String profileImg
) {
}
