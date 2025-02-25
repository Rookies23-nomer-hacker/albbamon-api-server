package com.api.domain.resume.repository;

import com.api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Resume_userRepository extends JpaRepository<User, Long> {
	Optional<User> findUserById(Long userId);
	
	
}


