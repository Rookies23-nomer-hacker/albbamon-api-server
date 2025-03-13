package com.api.domain.notice.entity;

import com.api.domain.notice.dto.request.UpdateNoticeRequestDto;
import com.api.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Setter
@Table(name = "notice")
@Entity
public class Notice extends BaseTimeEntity {
    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contents;

    public void updateNotice(UpdateNoticeRequestDto requestDto) {
        this.title = requestDto.title();
        this.contents = requestDto.contents();
    }
}
