package com.api.domain.post.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.post.dto.request.CreatePostRequestDto;
import com.api.domain.post.entity.Post;
import static com.api.domain.post.error.PostErrorCode.POST_NOT_FOUND;
import com.api.domain.post.mapper.PostMapper;
import com.api.domain.post.repository.PostRepo;
import com.api.domain.post.repository.PostRepository;
import com.api.domain.post.vo.PostListVo;
import com.api.domain.post.vo.PostVo;
import com.api.domain.user.entity.User;
import static com.api.domain.user.error.UserErrorCode.SIGN_IN_REQUIRED;
import static com.api.domain.user.error.UserErrorCode.USER_NOT_FOUND;
import com.api.domain.user.repository.UserRepository;
import com.api.global.error.exception.EntityNotFoundException;
import com.api.global.error.exception.UnauthorizedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.sql.Timestamp;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostRepo postRepo;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    // 모든 게시물 조회
    public List<PostListVo> getAllPosts() {
        return postRepository.findPostList();
    }

    public List<PostListVo> getSearchPostList(String keyword) {
	    List<Object[]> results = postRepo.findSearchPostList(keyword);
	
	    return results.stream().map(obj -> new PostListVo(
	        ((Number) obj[0]).longValue(),  // post_id
	        (String) obj[1],                   // title
	        (String) obj[2],                   // contents
	        ((Timestamp) obj[3]).toLocalDateTime(),  // create_date
	        (String) obj[4]                    // user_name
	    )).collect(Collectors.toList());
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
        postRepository.save(post);
    }

    public void deletePost(Long userId, Long postId) {
    // ✅ 로그인 확인
    if (userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);

    // ✅ 게시글 찾기 (없으면 예외 발생)
    Post post = postRepository.findPostById(postId)
            .orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));


    // ✅ 게시글 삭제
    postRepository.deleteById(postId);
        System.out.println("✅ 게시글 삭제 성공 - Post ID: " + postId);
    }


    public PostVo findById(Long postId) {
        return postRepository.findPostVoById(postId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
    }
    
}
