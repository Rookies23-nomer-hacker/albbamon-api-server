package com.api.domain.user.mapper;

import com.api.domain.user.dto.response.GetUserInfoResponseDto;
import com.api.domain.user.vo.UserVo;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public GetUserInfoResponseDto toGetUserInfoResponseDto(UserVo userVo) {
        return GetUserInfoResponseDto.of(userVo);
    }
}
