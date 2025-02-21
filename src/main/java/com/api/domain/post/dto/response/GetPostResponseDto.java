package com.api.domain.post.dto.response;

import java.util.List;

import com.api.domain.post.vo.PostVo;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record GetPostResponseDto(
        List<PostVo> postList
) {
    public static GetPostResponseDto of(List<PostVo> postList) {
        return GetPostResponseDto.builder()
                .postList(postList)
                .build();
    }
}
