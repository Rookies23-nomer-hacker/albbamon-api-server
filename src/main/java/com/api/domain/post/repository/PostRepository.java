package com.api.domain.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.domain.post.entity.Post;
import com.api.domain.post.vo.PostListVo;
import com.api.domain.post.vo.PostVo;

public interface PostRepository extends JpaRepository<Post, Long> {

        Optional<Post> findPostById(Long id);

        @Query(value = "SELECT new com.api.domain.post.vo.PostVo(p.id, u.id, p.title, p.contents, p.file, p.createDate, u.name) " +
            "FROM Post p " +
            "LEFT JOIN User u ON p.user = u " +
            "WHERE p.id = :postId")
        Optional<PostVo> findPostVoById(@Param("postId") Long postId);

    
        @Query("SELECT new com.api.domain.post.vo.PostListVo(p.id, p.title, p.contents, p.createDate, u.name) " +
       "FROM Post p LEFT JOIN p.user u ORDER BY p.createDate DESC")
        Page<PostListVo> findPostList(Pageable pageable);

        @Query(value = "SELECT new com.api.domain.post.vo.PostVo(p.id, u.id, p.title, p.contents, p.file, p.createDate, u.name) " +
        "FROM Post p " +
        "WHERE p.title LIKE CONCAT('%', ?1, '%') " +
        "OR p.contents LIKE CONCAT('%', ?1, '%') " +
        "LEFT JOIN p.user u " +
        "ORDER BY p.create_date DESC",
        nativeQuery = true)
        List<PostListVo> findSearchPostList(String keyword);
}
