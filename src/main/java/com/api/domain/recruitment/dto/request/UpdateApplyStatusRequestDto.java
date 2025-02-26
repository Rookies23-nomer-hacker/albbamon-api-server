package com.api.domain.recruitment.dto.request;

import com.api.domain.apply.type.ApplyStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateApplyStatusRequestDto {
	@JsonProperty("status")
    private String status;  // String으로 status를 받는다

    // status 값을 ApplyStatus enum으로 변환
	public ApplyStatus getStatusAsEnum() {
	    switch (this.status.toUpperCase()) {
	        case "PASSED":
	            return ApplyStatus.PASSED;
	        case "FAILED":
	            return ApplyStatus.FAILED;
	        case "WAITING":
	            return ApplyStatus.WAITING;
	        default:
	            throw new IllegalArgumentException("잘못된 상태 값입니다: " + this.status);
	    }
	}
}
