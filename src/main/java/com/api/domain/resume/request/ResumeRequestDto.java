package com.api.domain.resume.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
public record ResumeRequestDto(
		Long user_id,
        @NotNull String school,
        @NotNull String status,
        @NotNull String personal,
        @NotNull String work_place_region,
        @NotNull String work_place_city,
        @NotNull String industry_occupation,
        @NotNull String employmentType,
        @NotNull String working_period,
        @NotNull String working_day,
        @NotNull String introduction,
        String portfolioData,
        String portfoliourl,
        String portfolioName,
        String resume_imgurl,
        String resume_img_name,
        String resume_img_data,
        
        @NotNull LocalDateTime create_date,
        
        LocalDateTime last_modified_date) {//

}
