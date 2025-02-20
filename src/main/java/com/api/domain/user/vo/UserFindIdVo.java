package com.api.domain.user.vo;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record UserFindIdVo(
		String email,
		String phone
		) {

	//아이디찾기

}
