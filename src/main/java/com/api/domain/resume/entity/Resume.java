package com.api.domain.resume.entity;

import com.api.domain.career.entity.Career;
import com.api.domain.resume.request.ResumeRequestDto;
import com.api.domain.user.dto.request.CreateUserRequestDto;
import com.api.domain.user.entity.User;
import com.api.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private String personal;
    private String work_place_region;
    private String work_place_city;
    private String industry_occupation;
    private String employmentType;
    private String working_period;
    private String working_day;
    private String introduction;
    private String portfoliourl;
    private String portfolioname;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    private String resume_imgurl;
    private String resume_imgname;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Career> careerList = new ArrayList<>();
    
    
    public static Resume createResume(User user2, ResumeRequestDto resumerequestDto) {
    	System.out.println("createResume : "+resumerequestDto);
        return Resume.builder()
        		.user(user2)
                .school(resumerequestDto.school())
                .status(resumerequestDto.status())
                .personal(resumerequestDto.personal())
                .work_place_region(resumerequestDto.work_place_region())
                .work_place_city(resumerequestDto.work_place_city())
                .industry_occupation(resumerequestDto.industry_occupation())
                .employmentType(resumerequestDto.employmentType())
                .working_period(resumerequestDto.working_period())
                .working_day(resumerequestDto.working_day())
                .introduction(resumerequestDto.introduction())
                .portfoliourl(resumerequestDto.portfoliourl())
                .portfolioname(resumerequestDto.portfolioName())
                .createDate(resumerequestDto.create_date())
                .lastModifiedDate(resumerequestDto.last_modified_date())
                .resume_imgurl(resumerequestDto.resume_imgurl())
                .resume_imgname(resumerequestDto.resume_img_name())
                .build();
    }
    
    public static Resume duplicated(User user2, ResumeRequestDto resumerequestDto) {
    	System.out.println("resumerequestDto : "+resumerequestDto);
        return Resume.builder()
        		.user(user2)
                .school(resumerequestDto.school())
                .status(resumerequestDto.status())
                .personal(resumerequestDto.personal())
                .work_place_region(resumerequestDto.work_place_region())
                .work_place_city(resumerequestDto.work_place_city())
                .industry_occupation(resumerequestDto.industry_occupation())
                .employmentType(resumerequestDto.employmentType())
                .working_period(resumerequestDto.working_period())
                .working_day(resumerequestDto.working_day())
                .introduction(resumerequestDto.introduction())
                .portfoliourl(resumerequestDto.portfoliourl())
                .portfolioname(resumerequestDto.portfolioName())
                .createDate(resumerequestDto.create_date())
                .lastModifiedDate(resumerequestDto.last_modified_date())
                .build();
    }
 
}
