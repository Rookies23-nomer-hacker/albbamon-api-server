package com.api.domain.resume.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ResumeProfileImageRequestDto {
    @NotNull
    private String resume_img_data; // Base64 인코딩된 이미지 데이터

    public ResumeProfileImageRequestDto(String resume_img_data) {
        this.resume_img_data = resume_img_data;
    }
}