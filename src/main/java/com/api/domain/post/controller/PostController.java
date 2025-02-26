package com.api.domain.post.controller;

import static com.api.domain.user.controller.UserController.SESSION_NAME;

import com.api.domain.post.vo.PostListVo;
import com.api.domain.post.vo.PostVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.api.domain.post.dto.request.CreatePostRequestDto;
import com.api.domain.post.dto.response.GetPostResponseDto;
import com.api.domain.post.entity.Post;
import com.api.domain.post.service.PostService;
import com.api.global.common.entity.SuccessResponse;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Post")
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 목록 보기", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/list")
    public List<PostListVo> getAllPosts() {
        return postService.getAllPosts();
    }

    @Operation(summary = "게시글 검색", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/search")
    public List<PostListVo> getSearchPostList(@RequestParam("keyword") String keyword) {
        return postService.getSearchPostList(keyword);
    }

    @Operation(summary = "게시글 작성", responses = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true)
    })
    @PostMapping("/write")
    public ResponseEntity<SuccessResponse<?>> createPost(@RequestBody @Valid final CreatePostRequestDto requestDto) {
        postService.createPost(requestDto.userid(), requestDto);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "게시글 1건 조회", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/{postId:\\d}")
    public ResponseEntity<SuccessResponse<?>> getPostById(@PathVariable("postId") Long postId) {
        PostVo postVo = postService.findById(postId);
        return SuccessResponse.ok(postVo);
    }

    @Operation(summary = "게시글 수정", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @PostMapping("/update/{postId:\\d}")
    public ResponseEntity<SuccessResponse<?>> updatePost(@PathVariable final Long postId, 
                                                     @RequestBody @Valid final CreatePostRequestDto requestDto) {
    System.out.println("수정 요청 - Post ID: " + postId);
    System.out.println("수정 요청 - Title: " + requestDto.title());
    System.out.println("수정 요청 - Contents: " + requestDto.contents());

    postService.updatePost(requestDto.userid(), postId, requestDto);
    return SuccessResponse.ok(null);
    }
}
