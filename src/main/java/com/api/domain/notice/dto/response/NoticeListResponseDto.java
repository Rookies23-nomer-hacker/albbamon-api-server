package com.api.domain.notice.dto.response;

import com.api.domain.notice.entity.Notice;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record NoticeListResponseDto(
        List<Notice> noticeList
) {
    public static NoticeListResponseDto of(List<Notice> noticeList) {
        return NoticeListResponseDto.builder()
                .noticeList(noticeList)
                .build();
    }
}
