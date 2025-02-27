package com.api.domain.post.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

@CrossOrigin(origins = "http://localhost:60083", allowCredentials = "true")
@RequiredArgsConstructor
@RestController
@Tag(name = "Post")
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    // @Value("${upload.post.path:C:/Users/r2com/Documents/GitHub/albbamon-api-server/src/main/webapp/uploads/post/}")
    
    @Value("${upload.post.path:D:/abbamon/albbamon-api-server/src/main/webapp/uploads/post/}")
    private String uploadDir;
    
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
    public ResponseEntity<SuccessResponse<?>> createPost(
            @RequestParam(value = "file", required = false) MultipartFile file,  // ✅ @RequestPart → @RequestParam 변경
            @RequestParam("userId") Long userId,
            @RequestParam("title") String title, 
            @RequestParam("contents") String contents) {  

        System.out.println("게시글 작성 요청 - userId: " + userId);

        String filePath = null;
        if (file != null && !file.isEmpty()) {
            try {
                filePath = saveFile(file);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body(new SuccessResponse<>("파일 업로드 오류"));
            }
        }

        CreatePostRequestDto requestDto = new CreatePostRequestDto(userId, title, contents, filePath);
        postService.createPost(requestDto.userid(), requestDto);

        return SuccessResponse.ok(null);
    }



    private String saveFile(MultipartFile file) throws IOException {
        String directory = uploadDir;
        Path path = Paths.get(directory);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(directory + fileName);
        Files.copy(file.getInputStream(), filePath);

        return filePath.toString().replace("\\", "/");
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

    // 📌 게시글 수정 (파일 수정 X, 제목과 내용만 수정)
   @PostMapping("/update/{postId}")
    public ResponseEntity<SuccessResponse<?>> updatePost(
            @PathVariable("postId") final Long postId,
            @RequestBody Map<String, Object> requestBody) {  // ✅ JSON 데이터를 받도록 설정

        System.out.println("📌 요청 데이터 (requestBody): " + requestBody);

        // ✅ 요청에서 userId, title, contents 추출
        Long userId = requestBody.get("userid") != null ? Long.parseLong(requestBody.get("userid").toString()) : null;
        String title = requestBody.get("title") != null ? requestBody.get("title").toString() : null;
        String contents = requestBody.get("contents") != null ? requestBody.get("contents").toString() : null;

        // ✅ 로그 확인
        System.out.println("✅ 게시글 수정 요청 - postId: " + postId + ", userId: " + userId);

        if (userId == null) {
            System.out.println("🚨 userId가 null입니다! 로그인 세션 확인 필요.");
            return ResponseEntity.status(401).body(new SuccessResponse<>("로그인이 필요한 서비스입니다."));
        }

        // ✅ 게시글 수정 요청 처리 (파일 수정 X)
        CreatePostRequestDto requestDto = new CreatePostRequestDto(userId, title, contents, null);
        postService.updatePost(userId, postId, requestDto);

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

        Long userId = requestBody.get("userId") != null ? Long.parseLong(requestBody.get("userId").toString()) : null;
        postService.deletePost(userId, postId);
        return SuccessResponse.ok(null);
    } 
    
    @Operation(summary = "모바일 게시글 삭제", responses = {
        @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @DeleteMapping("/mobile/delete/{postId}")
    public ResponseEntity<SuccessResponse<?>> mobiledeletePost(
            @PathVariable final Long postId, 
            @RequestParam final Long userId) {  

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
        return postService.getSearchPostList(keyword);
    }
}