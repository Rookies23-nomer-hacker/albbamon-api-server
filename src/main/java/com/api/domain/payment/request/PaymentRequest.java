package com.api.domain.payment.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private String buyerName;
    private String buyerEmail;
    private String buyerTel;
    private String buyerAddr;
    private String buyerPostcode;
    private Long userId;

    // 기본 생성자
    public PaymentRequest() {}

    // 모든 필드를 초기화하는 생성자
    public PaymentRequest(String buyerName, String buyerEmail, String buyerTel, 
                          String buyerAddr, String buyerPostcode, Long userId) {
        this.buyerName = buyerName;
        this.buyerEmail = buyerEmail;
        this.buyerTel = buyerTel;
        this.buyerAddr = buyerAddr;
        this.buyerPostcode = buyerPostcode;
        this.userId = userId;
    }
}