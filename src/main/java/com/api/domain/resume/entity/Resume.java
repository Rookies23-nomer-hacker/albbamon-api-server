package com.api.domain.resume.entity;

import com.api.domain.career.entity.Career;
import com.api.domain.resume.request.ResumeRequestDto;
import com.api.domain.user.dto.request.CreateUserRequestDto;
import com.api.domain.user.entity.User;
import com.api.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "resume")
@Entity
public class Resume extends BaseTimeEntity {
    @Id
    @Column(name = "resume_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String school;
    private String status;
    private String work_place_region;
    private String work_place_city;
    private String industry_occupation;
    private String employmentType;
    private String working_period;
    private String working_day;
    private String introduction;
    private String portfolioData;
    private String portfolioName;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Career> careerList = new ArrayList<>();
    
    
    public static Resume createResume(ResumeRequestDto resumerequestDto) {
        return Resume.builder()
                .school(resumerequestDto.school())
                .status(resumerequestDto.status())
                .work_place_region(resumerequestDto.work_place_region())
                .work_place_city(resumerequestDto.work_place_city())
                .industry_occupation(resumerequestDto.industry_occupation())
                .employmentType(resumerequestDto.employmentType())
                .working_period(resumerequestDto.working_period())
                .working_day(resumerequestDto.working_day())
                .introduction(resumerequestDto.introduction())
                .portfolioData(resumerequestDto.portfolioData())
                .portfolioName(resumerequestDto.portfolioName())
                .build();
    }
    
    
}
