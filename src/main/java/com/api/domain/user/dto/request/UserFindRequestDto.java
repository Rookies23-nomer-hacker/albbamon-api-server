package com.api.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;
//아이디찾기
@Getter
@Setter
public class UserFindRequestDto {
    private String name;
    private String phone;
    private String ceoNum;
}