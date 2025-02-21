package com.api.domain.resume.repository;

import com.api.domain.resume.entity.Resume;
import com.api.domain.user.entity.User;
import com.api.domain.user.vo.UserVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Resume_userRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	Optional<User> findUserByEmail(String email);
	
}


