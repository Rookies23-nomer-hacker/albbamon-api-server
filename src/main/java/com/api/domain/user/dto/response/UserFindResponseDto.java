package com.api.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
//아이디찾기
@Getter
@Setter
@Builder
public class UserFindResponseDto {
    private String email;
    private String phone;
    private String type;
    private boolean success;
}