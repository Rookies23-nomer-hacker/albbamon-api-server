package com.api.domain.resume.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumeDto {
    private Long id;
    private String school;
    private String status;
    private String personal;
    private String workPlaceRegion;
    private String workPlaceCity;
    private String industryOccupation;
    private String employmentType;
    private String workingPeriod;
    private String workingDay;
    private String introduction;
    private String portfolioUrl;
    private String portfolioName;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    private String resumeImgUrl;
    private String resumeImgName;
}
