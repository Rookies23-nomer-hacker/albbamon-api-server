package com.api.domain.post.controller;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.domain.post.dto.request.CreatePostRequestDto;
import com.api.domain.post.service.PostService;
import com.api.domain.post.vo.PostListVo;
import com.api.domain.post.vo.PostVo;
import com.api.global.common.entity.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

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
    public List<PostListVo> getAllPosts(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getAllPosts(pageable);
    }


    @Operation(summary = "게시글 작성", responses = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true)
    })
    @PostMapping("/write")
    public ResponseEntity<SuccessResponse<?>> createPost(@RequestParam(value = "file", required = false) MultipartFile file,
                                                         @RequestParam("userId") Long userId,
                                                         @RequestParam("title") String title,
                                                         @RequestParam("contents") String contents,
                                                         HttpServletRequest request) {
        String serverUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        postService.createPost(userId, title, contents, file, serverUrl);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "게시글 1건 조회", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/{postId}")
    public ResponseEntity<SuccessResponse<?>> getPostById(@PathVariable("postId") Long postId) {
        PostVo postVo = postService.findById(postId);
        return SuccessResponse.ok(postVo);
    }

   @PostMapping("/update/{postId}")
    public ResponseEntity<SuccessResponse<?>> updatePost(@PathVariable("postId") final Long postId,
                                                         @RequestBody Map<String, Object> requestBody) {  // ✅ JSON 데이터를 받도록 설정
        // ✅ 요청에서 userId, title, contents 추출
        Long userId = requestBody.get("userid") != null ? Long.parseLong(requestBody.get("userid").toString()) : null;
        String title = requestBody.get("title") != null ? requestBody.get("title").toString() : null;
        String contents = requestBody.get("contents") != null ? requestBody.get("contents").toString() : null;

        if (userId == null) {
            System.out.println("🚨 userId가 null입니다! 로그인 세션 확인 필요.");
            return ResponseEntity.status(401).body(new SuccessResponse<>("로그인이 필요한 서비스입니다."));
        }

        // ✅ 게시글 수정 요청 처리 (파일 수정 X)
        CreatePostRequestDto requestDto = new CreatePostRequestDto(userId, title, contents, null);
        postService.updatePost(userId, postId, requestDto);

        return SuccessResponse.ok(null);
    }

    @Operation(summary = "게시글 삭제", responses = {
        @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<SuccessResponse<?>> deletePost(
            @PathVariable("postId") final Long postId,
            @RequestBody Map<String, Object> requestBody) {  

        Long userId = requestBody.get("userId") != null ? Long.parseLong(requestBody.get("userId").toString()) : null;
        postService.deletePost(userId, postId);
        return SuccessResponse.ok(null);
    }
    
    @Operation(summary = "게시글 검색", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/search")
    public List<PostListVo> getSearchPostList(@RequestParam("keyword") String keyword) {
        return postService.getSearchPostList(keyword);
    }
}