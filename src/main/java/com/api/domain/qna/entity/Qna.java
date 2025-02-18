package com.api.domain.qna.entity;

import com.api.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "qna")
@Entity
public class Qna extends BaseTimeEntity {
    @Id
    @Column(name = "qna_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
