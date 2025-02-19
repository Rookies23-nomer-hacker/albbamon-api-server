package com.api.domain.recruitment.repository;

import com.api.domain.recruitment.entity.Recruitment;
import com.api.domain.recruitment.vo.RecruitmentVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    Optional<Recruitment> findRecruitmentById(Long id);

    @Query(value = "SELECT new com.api.domain.recruitment.vo.RecruitmentVo(r.id, r.title, r.dueDate, r.contents, u.name) " +
            "FROM Recruitment r " +
            "LEFT JOIN User u ON r.user = u " +
            "ORDER BY r.createDate desc")
    List<RecruitmentVo> findAllRecruitmentVos();
}
