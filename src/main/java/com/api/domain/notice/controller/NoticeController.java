package com.api.domain.notice.controller;

import com.api.domain.notice.dto.request.UpdateNoticeRequestDto;
import com.api.domain.notice.dto.response.NoticeListResponseDto;
import com.api.domain.notice.dto.response.NoticeResponseDto;
import com.api.domain.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "공지사항 리스트", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping
    public ResponseEntity<?> getNoticeList() {
        NoticeListResponseDto responseDto = noticeService.getNoticeList();
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "공지사항 1개 상세 보기", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/{noticeId}")
    public ResponseEntity<?> getNotice(@PathVariable final Long noticeId) {
        NoticeResponseDto responseDto = noticeService.getNotice(noticeId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "공지사항 수정", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @PostMapping("/{noticeId}")
    public ResponseEntity<?> updateNotice(@PathVariable final Long noticeId,
                                          @RequestBody final UpdateNoticeRequestDto requestDto) {
        noticeService.updateNotice(noticeId, requestDto);
        return ResponseEntity.ok(null);
    }

}
