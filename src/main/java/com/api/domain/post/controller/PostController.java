package com.api.domain.post.controller;

import static com.api.domain.user.controller.UserController.SESSION_NAME;

import com.api.domain.post.service.PostService;
import com.api.domain.post.vo.PostListVo;
import com.api.domain.post.vo.PostVo;
import com.api.global.common.entity.SuccessResponse;
import com.api.domain.post.dto.request.CreatePostRequestDto;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Post")
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    // 📌 게시글 목록 조회
    @Operation(summary = "게시글 목록 보기", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/list")
    public List<PostListVo> getAllPosts() {
        return postService.getAllPosts();
    }

    // 📌 게시글 작성
    @Operation(summary = "게시글 작성", responses = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true)
    })
    @PostMapping("/write")
    public ResponseEntity<SuccessResponse<?>> createPost(@RequestBody @Valid final CreatePostRequestDto requestDto) {
        postService.createPost(requestDto.userid(), requestDto);
        return SuccessResponse.ok(null);
    }

    // 📌 게시글 1건 조회
    @Operation(summary = "게시글 1건 조회", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/{postId}")
    public ResponseEntity<SuccessResponse<?>> getPostById(@PathVariable("postId") Long postId) {
        PostVo postVo = postService.findById(postId);
        return SuccessResponse.ok(postVo);
    }

    // 📌 게시글 수정
    @Operation(summary = "게시글 수정", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @PostMapping("/update/{postId}")
    public ResponseEntity<SuccessResponse<?>> updatePost(
    	@PathVariable("postId") final Long postId,
        @RequestBody @Valid final CreatePostRequestDto requestDto) {  

        System.out.println("✅ 게시글 수정 요청 - Post ID: " + postId);
        System.out.println("✅ 수정 요청 - Title: " + requestDto.title());
        System.out.println("✅ 수정 요청 - Contents: " + requestDto.contents());

        postService.updatePost(requestDto.userid(), postId, requestDto);
        return SuccessResponse.ok(null);
    }

    // 📌 게시글 삭제
    @Operation(summary = "게시글 삭제", responses = {
        @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<SuccessResponse<?>> deletePost(
    		@PathVariable("postId") final Long postId,
            @RequestBody Map<String, Object> requestBody) {  

        // ✅ JSON에서 userId 추출
        Long userId = requestBody.get("userId") != null ? Long.parseLong(requestBody.get("userId").toString()) : null;

        System.out.println("✅ API 서버 - 삭제 요청 - 사용자 ID: " + userId + ", Post ID: " + postId);

        postService.deletePost(userId, postId);
        System.out.println("✅ 게시글 삭제 완료 - Post ID: " + postId);
        return SuccessResponse.ok(null);
    } 
    
    @Operation(summary = "게시글 검색", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/search")
    public List<PostListVo> getSearchPostList(@RequestParam("keyword") String keyword) {
        System.out.println("keyword : "+keyword);
        return postService.getSearchPostList(keyword);
    }




}