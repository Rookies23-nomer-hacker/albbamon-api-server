package com.api.domain.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.domain.post.entity.Post;
import com.api.domain.post.vo.PostVo;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 📌 단일 Post 조회
    Optional<Post> findPostById(Long id);

    // 📌 모든 PostVo 조회
    @Query(value = "SELECT new com.api.domain.post.vo.PostVo(p.id, p.title, p.contents, p.file, p.createDate, u.name) " +
            "FROM Post p " +
            "LEFT JOIN User u ON p.user = u " +
            "ORDER BY p.createDate DESC")
    List<PostVo> findAllPostVos();
}
