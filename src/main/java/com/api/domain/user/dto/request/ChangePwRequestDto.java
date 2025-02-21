package com.api.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePwRequestDto {
    private Long userId;         // 사용자 ID (웹 서버에서 Long으로 보냄)
    private String passwd;         // 현재 비밀번호
    private String newpasswd;      // 새 비밀번호
}