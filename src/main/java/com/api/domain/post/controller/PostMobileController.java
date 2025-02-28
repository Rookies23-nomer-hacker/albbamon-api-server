package com.api.domain.post.controller;

import com.api.domain.post.dto.request.CreatePostRequestDto;
import com.api.domain.post.service.PostService;
import com.api.global.common.entity.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:60083", allowCredentials = "true")
@RequiredArgsConstructor
@RestController
@Tag(name = "Post")
@RequestMapping("/api/mobile/post")
public class PostMobileController {
    private final PostService postService;

    @Operation(summary = "[모바일] 게시글 작성", responses = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true)
    })
    @PostMapping("/write")
    public ResponseEntity<SuccessResponse<?>> createPost(@SessionAttribute("userid") Long userId,
                                                         @RequestParam(value = "file", required = false) MultipartFile file,
                                                         @RequestParam("title") String title,
                                                         @RequestParam("contents") String contents) {
        postService.createPost(userId, title, contents, file);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "[모바일] 게시글 수정", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @PostMapping("/update/{postId}")
    public ResponseEntity<SuccessResponse<?>> updatePost(@SessionAttribute("userid") Long userId,
                                                         @PathVariable("postId") final Long postId,
                                                         @RequestBody CreatePostRequestDto requestDto) {
        postService.updatePost(userId, postId, requestDto);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "[모바일] 게시글 삭제", responses = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<SuccessResponse<?>> deletePostMobile(@SessionAttribute("userid") Long userId,
                                                               @PathVariable final Long postId) {
        postService.deletePost(userId, postId);
        return SuccessResponse.ok(null);
    }

    /*
    *     private String saveFile(MultipartFile file) throws IOException {
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
    *
    * */
}