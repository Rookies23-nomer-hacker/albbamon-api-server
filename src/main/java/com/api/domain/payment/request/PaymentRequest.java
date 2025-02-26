package com.api.domain.payment.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private String item;
    private Long userId;

    // 기본 생성자
    public PaymentRequest() {}

    // 모든 필드를 초기화하는 생성자
    public PaymentRequest(String item, Long userId) {
        this.item = item;
        this.userId = userId;
    }
}