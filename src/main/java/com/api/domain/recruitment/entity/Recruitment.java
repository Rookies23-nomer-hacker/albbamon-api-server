package com.api.domain.recruitment.entity;

import com.api.domain.apply.entity.Apply;
import com.api.domain.recruitment.dto.request.CreateRecruitmentRequestDto;
import com.api.domain.user.entity.User;
import com.api.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "recruitment")
@Entity
public class Recruitment extends BaseTimeEntity {
    @Id
    @Column(name = "recruitment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDateTime dueDate;

    private String contents;

    private Integer wage;

    private String file;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    
    // 결제한 사용자 조회
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @OneToMany(mappedBy = "recruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Apply> applyList = new ArrayList<>();

    public static Recruitment createRecruitment(User user, CreateRecruitmentRequestDto requestDto, String filePath) {
        return Recruitment.builder()
                .title(requestDto.title())
                .contents(requestDto.contents())
                .dueDate(requestDto.dueDate())
                .wage(requestDto.wage())
                .user(user)
                .file(filePath)
                .build();
    }

    public void updateRecruitment(CreateRecruitmentRequestDto requestDto) {
        this.title =  requestDto.title();
        this.contents = requestDto.contents();
        this.dueDate = requestDto.dueDate();
        this.wage = requestDto.wage();
    }
}
