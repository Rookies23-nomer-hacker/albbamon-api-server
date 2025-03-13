package com.api.domain.notice.controller;

import com.api.domain.notice.dto.request.UpdateNoticeRequestDto;
import com.api.domain.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Notice")
@RequestMapping("/api/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping("/{noticeId}")
    public ResponseEntity<?> updateNotice(@PathVariable final Long noticeId,
                                          @RequestBody final UpdateNoticeRequestDto requestDto) {
        noticeService.updateNotice(noticeId, requestDto);
        return ResponseEntity.ok(null);
    }

}
