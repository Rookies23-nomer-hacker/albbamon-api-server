package com.api.domain.post.vo;

import java.time.LocalDateTime;

public interface PostListProjection {
    Long getPostId();
    String getTitle();
    String getContents();
    LocalDateTime getCreateDate();
    String getUserName();
}