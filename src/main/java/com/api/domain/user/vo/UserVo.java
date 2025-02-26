package com.api.domain.user.vo;

import java.time.LocalDateTime;

import com.api.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public record UserVo(
        Long id,
        String name,
        String email,
        String phone,
        String ceoNum,
        String company,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime lastModifiedDate
) {
	public static UserVo of(User user, String encryptedName, String encryptedEnail, String encryptedPhone) {
		return UserVo.builder()
				.id(user.getId())
				.name(encryptedName)
				.email(encryptedEnail)
				.phone(encryptedPhone)
				.ceoNum(user.getCeoNum())
				.company(user.getCompany())
				.lastModifiedDate(user.getLastModifiedDate())
				.build();
	}
}
