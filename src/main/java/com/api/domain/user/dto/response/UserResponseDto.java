package com.api.domain.user.dto.response;

import com.api.domain.user.vo.UserVo;
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
    
    public UserResponseDto(UserVo userVo) {
        this.userId = userVo.id();
        this.email = userVo.email();
        this.name = userVo.name();
        this.ceoNum = userVo.ceoNum();
        this.company = userVo.company();
    }
}
