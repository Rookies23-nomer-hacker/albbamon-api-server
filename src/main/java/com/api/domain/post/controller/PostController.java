package com.api.domain.post.controller;

import static com.api.domain.user.controller.UserController.SESSION_NAME;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Tag(name = "Post")
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    /**
     * 게시글 목록 보기
     */
    @Operation(summary = "게시글 목록 보기", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/")
    public ResponseEntity<SuccessResponse<?>> getPostList() {
        GetPostResponseDto responseDto = postService.getPostList();
        return SuccessResponse.ok(responseDto);
    }

    /**
     * 게시글 작성
     */
    @Operation(summary = "게시글 작성", responses = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true)
    })
    @PostMapping("/write")
    public ResponseEntity<SuccessResponse<?>> createPost(@SessionAttribute(name = SESSION_NAME) Long userId,
                                                         @RequestBody @Valid final CreatePostRequestDto requestDto) {
        postService.createPost(userId, requestDto);
        return SuccessResponse.ok(null);
    }

    /**
     * 게시글 조회 (id로 조회)
     */
    @Operation(summary = "게시글 조회", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Post Not Found")
    })
    @GetMapping("/{postid}")
    public ResponseEntity<Post> getPostById(@PathVariable("postid") Long id) {
        Post post = postService.findById(id);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(post);
    }

    /**
     * 게시글 수정
     */
    @Operation(summary = "게시글 수정", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @PutMapping("/{postId}")
    public ResponseEntity<SuccessResponse<?>> updatePost(@SessionAttribute(name = SESSION_NAME) Long userId,
                                                         @PathVariable final Long postId,
                                                         @RequestBody @Valid final CreatePostRequestDto requestDto) {
        postService.updatePost(userId, postId, requestDto);
        return SuccessResponse.ok(null);
    }
}
