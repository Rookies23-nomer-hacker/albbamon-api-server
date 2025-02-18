package com.api.domain.post.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.post.entity.Post;
import com.api.domain.post.service.PostService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }
    
    @GetMapping("/{cmNum}")
    public Post getPost(@PathVariable Long cmNum) {
        return postService.getPostById(cmNum);
    }
}
