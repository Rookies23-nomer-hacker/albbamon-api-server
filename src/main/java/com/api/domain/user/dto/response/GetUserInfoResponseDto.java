package com.api.domain.user.dto.response;

import com.api.domain.user.vo.UserVo;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record GetUserInfoResponseDto(
        UserVo userInfo
) {
    public static GetUserInfoResponseDto of(UserVo userInfo) {
        return GetUserInfoResponseDto.builder()
                .userInfo(userInfo)
                .build();
    }
}
