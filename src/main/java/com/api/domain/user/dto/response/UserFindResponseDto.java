package com.api.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserFindResponseDto {
    private String email;
    private String phone;
    
    private boolean success;
}