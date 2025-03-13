package com.api.domain.notice.service;

import com.api.domain.notice.dto.request.UpdateNoticeRequestDto;
import com.api.domain.notice.dto.response.NoticeListResponseDto;
import com.api.domain.notice.dto.response.NoticeResponseDto;
import com.api.domain.notice.entity.Notice;
import com.api.domain.notice.repository.NoticeRepository;
import com.api.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.api.domain.notice.error.NoticeErrorCode.NOTICE_NOT_FOUND;

@RequiredArgsConstructor
@Transactional
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public void updateNotice(Long noticeId, UpdateNoticeRequestDto requestDto) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new EntityNotFoundException(NOTICE_NOT_FOUND));
        notice.updateNotice(requestDto);
        noticeRepository.save(notice);
    }

    public NoticeListResponseDto getNoticeList() {
        return NoticeListResponseDto.of(noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
    }

    public NoticeResponseDto getNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new EntityNotFoundException(NOTICE_NOT_FOUND));
        return NoticeResponseDto.of(notice);
    }
}
