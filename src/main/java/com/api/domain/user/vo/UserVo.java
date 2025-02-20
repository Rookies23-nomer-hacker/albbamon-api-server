package com.api.domain.user.vo;

import lombok.Builder;

@Builder
public record UserVo(
        Long id,
        String name,
        String email,
        String phone,
        String ceoNum,
        String company,
        String profileImg
) {
}
