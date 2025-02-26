package com.api.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.user.entity.User;
import com.api.domain.user.vo.UserVo;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Optional<User> findUserById(Long userId);
    Optional<User> findUserByEmail(String email);
    Optional<UserVo> findUserVoById(Long id);
    //아이디찾기
    //이름과 전화번호로 사용자 찾기
    List<User> findByNameAndPhone(String name, String phone);
    List<User> findByNameAndCeoNum(String name, String ceoNum); // 기업 회원 검색
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.item = :item WHERE u.id = :userId")
    void updateItemStatus(@Param("userId") Long userId, @Param("item") String item);
}
