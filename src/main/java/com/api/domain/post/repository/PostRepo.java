package com.api.domain.post.repository;
import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import com.api.domain.post.vo.PostListProjection;

@Repository
@RequiredArgsConstructor
public class PostRepo {

    private final EntityManager entityManager;

    public List<Object[]> findSearchPostList(String keyword) {
        // SQL Injection 취약점이 있는 쿼리
        String sql = "SELECT p.post_id, p.title, p.contents, p.create_date, u.name " +
                     "FROM post p " +
                     "LEFT JOIN user u ON p.user_id = u.user_id " +
                     "WHERE p.title LIKE '%" + keyword + "%' " +  // SQL Injection 가능
                     "ORDER BY p.create_date DESC";

        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }
}