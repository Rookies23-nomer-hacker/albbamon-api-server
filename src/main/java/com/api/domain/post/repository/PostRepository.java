package com.api.domain.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.domain.post.entity.Post;
import com.api.domain.post.vo.PostListVo;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    
	@Query("SELECT new com.api.domain.post.vo.PostListVo(p.postId, p.title, p.contents, p.createDate, u.name) " +
       "FROM Post p LEFT JOIN p.user u ORDER BY p.createDate DESC")
    List<PostListVo> findPostList();
}
