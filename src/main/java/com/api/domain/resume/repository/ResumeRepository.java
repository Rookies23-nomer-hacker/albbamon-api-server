package com.api.domain.resume.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.domain.resume.entity.Resume;
import com.api.domain.user.entity.User;

import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findResumeByUserId(Long userId);
	Resume findByuser_id(Long user_id);
	Optional<Resume> findUserByUser_id(Long user_id);
	void deleteById(Long id);
}
