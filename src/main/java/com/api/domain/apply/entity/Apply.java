package com.api.domain.apply.entity;

import java.time.LocalDateTime;

import com.api.domain.recruitment.entity.Recruitment;
import com.api.domain.resume.entity.Resume;
import com.api.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "apply")
@Entity
public class Apply extends BaseTimeEntity {
    @Id
    @Column(name = "apply_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long apply_id;
    
    @ManyToOne
    @JoinColumn(name = "recruitment_id", referencedColumnName = "recruitment_id")
    private Recruitment recruitment;
    
    @ManyToOne
    @JoinColumn(name = "resume_id", referencedColumnName = "resume_id")
    private Resume resume;
    
    @Column(name = "status")
    private Integer status;
    
    @Column(name = "create_date")
    private LocalDateTime createDate;
}
