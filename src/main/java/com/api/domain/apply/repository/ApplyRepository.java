package com.api.domain.apply.repository;

import com.api.domain.apply.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
    Apply findApplyByRecruitmentIdAndResumeId(Long recruitmentId, Long resumeId);
}
