package com.api.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFindRequestDto {
    private String name;
    private String phone;
}