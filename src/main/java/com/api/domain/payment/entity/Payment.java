package com.api.domain.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "payment")
@Entity
public class Payment {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id")
    private Long pay_id;
    
    @Column(name = "buyer_name")
    private String buyerName;
    
    @Column(name = "buyer_email")
    private String buyerEmail;
    
    @Column(name = "buyer_post_code")
    private String buyerPostCode;
    
    @Column(name = "buyer_addr")
    private String buyerAddr;
    
    @Column(name = "buyer_tel")
    private String buyerTel;
    
    @Column(name = "user_id")
    private Long userId;

}
