package com.api.domain.post.vo;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record PostListVo(
        Long postId,
        String title,
        String contents,
        LocalDateTime createDate,
        String userName
) {
}
