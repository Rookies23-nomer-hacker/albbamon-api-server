package com.api.domain.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.domain.post.entity.Post;
import com.api.domain.post.vo.PostListProjection;
import com.api.domain.post.vo.PostListVo;
import com.api.domain.post.vo.PostVo;

public interface PostRepository extends JpaRepository<Post, Long> {

        Optional<Post> findPostById(Long id);

        @Query(value = "SELECT new com.api.domain.post.vo.PostVo(p.id, u.id, p.title, p.contents, p.file, p.createDate, u.name) " +
            "FROM Post p " +
            "LEFT JOIN User u ON p.user = u " +
            "WHERE p.id = :postId")
        Optional<PostVo> findPostVoById(Long postId);

    
        @Query("SELECT new com.api.domain.post.vo.PostListVo(p.id, p.title, p.contents, p.createDate, u.name) " +
       "FROM Post p LEFT JOIN p.user u ORDER BY p.createDate DESC")
        List<PostListVo> findPostList();

        @Query(value = "select p.post_id AS postId, p.title AS title, p.contents AS contents, p.create_date AS createDate,u.name AS userName from post p LEFT JOIN user u on p.user_id = u.user_id WHERE p.title LIKE :keyword ORDER BY p.create_date DESC;",
            nativeQuery = true)
        List<PostListProjection> findSearchPostList_nosqlInejction(String keyword);
    
}


