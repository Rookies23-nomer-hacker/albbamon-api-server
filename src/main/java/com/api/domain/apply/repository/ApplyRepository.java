package com.api.domain.apply.repository;

import com.api.domain.apply.entity.Apply;
import com.api.domain.apply.vo.ApplyVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
    Apply findApplyByRecruitmentIdAndResumeId(Long recruitmentId, Long resumeId);

    @Query(value = "SELECT new com.api.domain.apply.vo.ApplyVo(a.id, rec.title, rec.wage, a.createDate, a.status) " +
            "FROM Apply a " +
            "LEFT JOIN Resume res ON a.resume = res " +
            "LEFT JOIN Recruitment rec ON a.recruitment = rec " +
            "WHERE res.user.id = :userId " +
            "ORDER BY a.createDate desc")
    List<ApplyVo> findApplyVoByUserId(Long userId);
}
