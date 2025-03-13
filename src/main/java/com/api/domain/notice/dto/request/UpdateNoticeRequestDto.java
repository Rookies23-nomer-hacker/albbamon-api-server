package com.api.domain.notice.dto.request;

public record UpdateNoticeRequestDto(
        String title,
        String contents
) {
}
