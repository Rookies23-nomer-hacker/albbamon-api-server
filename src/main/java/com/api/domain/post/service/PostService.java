package com.api.domain.post.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.domain.post.entity.Post;
import com.api.domain.post.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;

    // 생성자를 통한 의존성 주입
    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 모든 게시물 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 게시물 ID로 조회
    public Post getPostById(Long cmNum) {
        Optional<Post> post = postRepository.findById(cmNum);
        return post.orElse(null);
    }
}