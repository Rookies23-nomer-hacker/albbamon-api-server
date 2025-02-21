package com.api.domain.post.vo;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record PostVo(
        Long postId,
        String title,
        String contents,
        String file,
        LocalDateTime createDate,
        String userName
) {
}
