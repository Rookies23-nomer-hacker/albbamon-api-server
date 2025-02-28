package com.api.domain.post.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import com.api.global.common.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.post.dto.request.CreatePostRequestDto;
import com.api.domain.post.entity.Post;
import static com.api.domain.post.error.PostErrorCode.POST_NOT_FOUND;
import com.api.domain.post.repository.PostRepo;
import com.api.domain.post.repository.PostRepository;
import com.api.domain.post.vo.PostListVo;
import com.api.domain.post.vo.PostVo;
import com.api.domain.user.entity.User;
import static com.api.domain.user.error.UserErrorCode.SIGN_IN_REQUIRED;
import static com.api.domain.user.error.UserErrorCode.USER_NOT_FOUND;
import com.api.domain.user.repository.UserRepository;
import com.api.global.common.util.XorDecryptUtil;
import com.api.global.error.exception.EntityNotFoundException;
import com.api.global.error.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {
    private static final String POST_FILE_DIR = "/home/api_root/download/apache-tomcat-10.1.36/webapps/ROOT/upload/post/";
    private final PostRepository postRepository;
    private final PostRepo postRepo;
    private final UserRepository userRepository;

    private final FileUtil fileUtil;

    @Value("${spring.datasource.encryption-key}")
  	private String encryptionKey;

    public List<PostListVo> getAllPosts() {
    List<PostListVo> postlistvo = postRepository.findPostList();
        return postlistvo.stream()
            .map(post -> new PostListVo(
                post.postId(),
                post.title(),
                post.contents(),
                post.createDate(),
                XorDecryptUtil.xorDecrypt(post.userName(), encryptionKey)
            ))
            .toList();
    }

    public void createPost(Long userId, String title, String contents, MultipartFile file) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        User user = userRepository.findUserById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        String filePath = fileUtil.saveFile(file, POST_FILE_DIR);
        Post post = Post.createPost(user, title, contents, filePath);
        postRepository.save(post);
    }

    public void updatePost(Long userId, Long postId, CreatePostRequestDto requestDto) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        Post post = postRepository.findPostById(postId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        post.updatePost(requestDto);
        postRepository.save(post);
    }

    public void deletePost(Long userId, Long postId) {
        if (userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        Post post = postRepository.findPostById(postId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        postRepository.deleteById(post.getId());
    }

    public List<PostListVo> getSearchPostList(String keyword) {
        List<Object[]> results = postRepo.findSearchPostList(keyword);
        return results.stream().map(obj -> new PostListVo(
            ((Number) obj[0]).longValue(),
            (String) obj[1],
            (String) obj[2],
            ((Timestamp) obj[3]).toLocalDateTime(),
            XorDecryptUtil.xorDecrypt((String) obj[4], encryptionKey)
        )).collect(Collectors.toList());
    }

    public PostVo findById(Long postId) {
        PostVo postvo = postRepository.findPostVoById(postId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        PostVo post = new PostVo(
            postvo.postId(),
            postvo.userId(),
            postvo.title(),
            postvo.contents(),
            postvo.file(),
            postvo.createDate(),
            XorDecryptUtil.xorDecrypt(postvo.userName(), encryptionKey) // 이름 복호화
        );
        return post;
    }
    
}