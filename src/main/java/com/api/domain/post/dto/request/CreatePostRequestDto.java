package com.api.domain.post.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record CreatePostRequestDto(
        @NotNull long postid,
        @NotNull String userid,
        @NotNull String title,
        @NotNull String contents,
        @NotNull String file,
        @NotNull LocalDateTime bday
        
) {
}
