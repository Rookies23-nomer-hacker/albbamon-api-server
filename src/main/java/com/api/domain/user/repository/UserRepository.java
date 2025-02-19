package com.api.domain.user.repository;

import com.api.domain.user.entity.User;
import com.api.domain.user.vo.UserVo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Optional<User> findUserByEmail(String email);
    Optional<UserVo> findUserVoById(Long id);
}
