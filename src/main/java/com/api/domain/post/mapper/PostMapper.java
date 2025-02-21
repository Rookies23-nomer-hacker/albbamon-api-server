package com.api.domain.post.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.api.domain.post.dto.response.GetPostResponseDto;
import com.api.domain.post.vo.PostVo;

@Component
public class PostMapper {
    public GetPostResponseDto toGetPostResponseDto(List<PostVo> postList) {
        return GetPostResponseDto.of(postList);
    }
}
