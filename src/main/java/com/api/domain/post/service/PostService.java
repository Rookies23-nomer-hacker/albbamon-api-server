package com.api.domain.post.service;

import static com.api.domain.post.error.PostErrorCode.POST_NOT_FOUND;
import static com.api.domain.user.error.UserErrorCode.SIGN_IN_REQUIRED;
import static com.api.domain.user.error.UserErrorCode.USER_NOT_FOUND;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.post.dto.request.CreatePostRequestDto;
import com.api.domain.post.dto.response.GetPostResponseDto;
import com.api.domain.post.vo.PostListVo;
import com.api.domain.post.entity.Post;
import com.api.domain.post.mapper.PostMapper;
import com.api.domain.post.repository.PostRepository;
import com.api.domain.post.vo.PostVo;
import com.api.domain.user.entity.User;
import com.api.domain.user.repository.UserRepository;
import com.api.global.error.exception.EntityNotFoundException;
import com.api.global.error.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    // 모든 게시물 조회
    public List<PostListVo> getAllPosts() {
        return postRepository.findPostList();
    }
    public List<PostListVo> getSearchlist(String keyword) {
        return postRepository.findSearchPostList(keyword);
    }

    public void createPost(Long userId, CreatePostRequestDto requestDto) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        User user = userRepository.findUserById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        Post post = Post.createPost(user, requestDto);
        postRepository.save(post);
    }
    


    public void updatePost(Long userId, Long postId, CreatePostRequestDto requestDto) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        Post post = postRepository.findPostById(postId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        post.updatePost(requestDto);
    }

    public PostVo findById(Long postId) {
        return postRepository.findPostVoById(postId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
    }
}
