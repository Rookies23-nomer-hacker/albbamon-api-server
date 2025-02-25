package com.api.domain.post.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record CreatePostRequestDto(
        Long userid,
        String title,
        String contents,
        String file
        
) {
}
