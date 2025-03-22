package com.api.domain.recruitment.repository;

import com.api.domain.recruitment.entity.Recruitment;
import com.api.domain.recruitment.vo.RecruitmentDetailVo;
import com.api.domain.recruitment.vo.RecruitmentVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    Optional<Recruitment> findRecruitmentById(Long id);

    @Query(value = "SELECT new com.api.domain.recruitment.vo.RecruitmentVo(r.id, r.title, r.dueDate, r.createDate, r.wage, r.file, u.company, u.name, u.email, u.ceoNum, u.item) " +
            "FROM Recruitment r " +
            "LEFT JOIN User u ON r.user = u " +
            "ORDER BY r.createDate desc")
    Page<RecruitmentVo> findAllRecruitmentVos(Pageable pageable);

    @Query(value = "SELECT new com.api.domain.recruitment.vo.RecruitmentVo(r.id, r.title, r.dueDate, r.createDate, r.wage, r.file, u.company, u.name, u.email, u.ceoNum, u.item) " +
            "FROM Recruitment r " +
            "JOIN User u ON r.user = u " +
            "WHERE u.id = :userId " +
            "ORDER BY r.createDate desc")
    Page<RecruitmentVo> findAllRecruitmentVosByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT new com.api.domain.recruitment.vo.RecruitmentDetailVo(r.id, r.title, r.dueDate, r.createDate, r.contents, r.wage, u.name, u.company, r.file) " +
            "FROM Recruitment r " +
            "JOIN User u ON r.user = u " +
            "WHERE r.id = :recruitmentId")
    Optional<RecruitmentDetailVo> findRecruitmentDetailVoById(@Param("recruitmentId") Long recruitmentId);

    Long countByUserId(Long userId);
}
