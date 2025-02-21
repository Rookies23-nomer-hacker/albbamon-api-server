package com.api.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

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
}
