package com.api.domain.post.entity;

import java.time.LocalDateTime;

import com.api.domain.post.dto.request.CreatePostRequestDto;
import com.api.domain.user.entity.User;
import com.api.global.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "post")
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String contents;

    private String file;

    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 📌 Post 생성 메서드 (정적 팩토리 메서드)
    public static Post createPost(User user, CreatePostRequestDto requestDto) {
        return Post.builder()
                .title(requestDto.title())
                .contents(requestDto.contents())
                .file(requestDto.file())
                .createDate(LocalDateTime.now())
                .user(user)
                .build();
    }

    // 📌 Post 수정 메서드
    public void updatePost(CreatePostRequestDto requestDto) {
        this.title = requestDto.title();
        this.contents = requestDto.contents();
        this.file = requestDto.file();
    }
}
