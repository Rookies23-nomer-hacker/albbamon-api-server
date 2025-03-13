package com.api.domain.notice.dto.response;

import com.api.domain.notice.entity.Notice;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record NoticeResponseDto(
        Notice notice
) {
    public static NoticeResponseDto of(Notice notice) {
        return NoticeResponseDto.builder()
                .notice(notice)
                .build();
    }
}
