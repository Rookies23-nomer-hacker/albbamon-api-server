package com.api.domain.post.dto.response;

import com.api.domain.post.vo.PostListVo;
import com.api.global.common.entity.PageInfo;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record GetPostListResponseDto(
        List<PostListVo> postList,
        PageInfo pageInfo
) {
    public static GetPostListResponseDto of(List<PostListVo> postList, PageInfo pageInfo) {
        return GetPostListResponseDto.builder()
                .postList(postList)
                .pageInfo(pageInfo)
                .build();
    }
}
