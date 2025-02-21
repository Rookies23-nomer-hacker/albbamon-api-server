package com.api.domain.resume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.domain.resume.entity.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

}
