package com.api.domain.user.dto.response;

import com.api.domain.user.entity.User;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
	private Long userId;
    private String email;
    private String name;
    private String ceoNum;
    private String company;
    
    public UserResponseDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.ceoNum = user.getCeoNum();
        this.company = user.getCompany();
    }
}
