package com.api.domain.apply.repository;

import com.api.domain.apply.entity.Apply;
import com.api.domain.apply.type.ApplyStatus;
import com.api.domain.apply.vo.ApplyVo;
import com.api.domain.apply.vo.RecruitmentApplyMobileVo;
import com.api.domain.apply.vo.RecruitmentApplyVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
    Apply findApplyByRecruitmentIdAndResumeId(Long recruitmentId, Long resumeId);

    @Query(value = "SELECT new com.api.domain.apply.vo.ApplyVo(a.id, rec.title, rec.wage, owner.company, a.createDate, a.status) " +
            "FROM Apply a " +
            "LEFT JOIN Resume res ON a.resume = res " +
            "LEFT JOIN Recruitment rec ON a.recruitment = rec " +
            "LEFT JOIN User owner ON rec.user = owner " +
            "WHERE res.user.id = :userId " +
            "ORDER BY a.createDate desc")
    List<ApplyVo> findApplyVoByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT new com.api.domain.apply.vo.RecruitmentApplyVo(a.id, res.id, u.name, res.school, res.status, res.personal, res.work_place_region, res.work_place_city, res.industry_occupation, res.employmentType, res.working_period, res.working_day, res.introduction, res.portfoliourl, res.portfolioname, res.resume_imgurl, res.resume_imgname, a.createDate, a.status) " +
            "FROM Apply a " +
            "LEFT JOIN Resume res ON a.resume = res " +
            "LEFT JOIN User u ON res.user = u " +
            "LEFT JOIN Recruitment rec ON a.recruitment = rec " +
            "WHERE rec.id = :recruitmentId " +
            "ORDER BY a.createDate desc")
    List<RecruitmentApplyVo> findRecruitmentApplyVoByRecruitmentId(@Param("recruitmentId") Long recruitmentId);

    @Query(value = "SELECT new com.api.domain.apply.vo.RecruitmentApplyMobileVo(a.id, res.id, u.name, u.email, u.phone, res.school, res.status, res.personal, res.work_place_region, res.work_place_city, res.industry_occupation, res.employmentType, res.working_period, res.working_day, res.introduction, res.portfoliourl, res.portfolioname, res.resume_imgurl, res.resume_imgname, a.createDate, a.status) " +
            "FROM Apply a " +
            "LEFT JOIN Resume res ON a.resume = res " +
            "LEFT JOIN User u ON res.user = u " +
            "LEFT JOIN Recruitment rec ON a.recruitment = rec " +
            "WHERE rec.id = :recruitmentId " +
            "ORDER BY a.createDate desc")
    List<RecruitmentApplyMobileVo> findRecruitmentApplyMobileVoByRecruitmentId(@Param("recruitmentId") Long recruitmentId);
    
    @Modifying
    @Query("UPDATE Apply a SET a.status = :status WHERE a.id = :applyId")
    void updateStatus(@Param("recruitmentId") Long recruitmentId, @Param("applyId") Long applyId, @Param("status") ApplyStatus status);

    Boolean existsByResumeIdAndRecruitmentId(@Param("resumeId")Long resumeId, @Param("recruitmentId") Long recruitmentId);

    Long countByResumeId(Long resumeId);
}
