package com.api.domain.recruitment.repository;

import com.api.domain.recruitment.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
}
