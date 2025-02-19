package com.api.domain.resume.service;

import com.api.domain.resume.entity.Resume;
import com.api.domain.resume.request.ResumeRequestDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ResumeService {
	
    public static void createResume(ResumeRequestDto resumerequestDto) {
    	
    	Resume.createResume(resumerequestDto);
        System.out.println("service"+resumerequestDto);
    }
}
