package com.api.domain.resume.service;

import com.api.domain.resume.entity.Resume;
import com.api.domain.resume.repository.ResumeRepository;
import com.api.domain.resume.repository.Resume_userRepository;
import com.api.domain.resume.request.ResumeRequestDto;
import com.api.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ResumeService {
	
	private final ResumeRepository resumeRepository;
	private final Resume_userRepository resumeUserRepository;
	
    public void createResume(ResumeRequestDto resumerequestDto) {
    	
    	Resume resume = Resume.createResume(resumerequestDto);
    	System.out.println("ddddddddd="+resumerequestDto);
    	resumeRepository.save(resume);
    }
    
    public Map<String, Object> getEmailById(String email) {
        User user = resumeUserRepository.findByEmail(email);
        Map<String,Object> json = new HashMap<>();
        json.put("email", user.getEmail());
        json.put("name", user.getName());
        json.put("phone", user.getPhone());
        return json;
    }
    
    public Map<String, Object> getresumeUser_id(Long user_id) {
    	Resume resume = resumeRepository.findByuser_id(user_id);
        Map<String,Object> json = new HashMap<>();
        json.put("personal", resume.getPersonal());
        json.put("work_place_region", resume.getWork_place_region());
        json.put("industry_occupation", resume.getIndustry_occupation());
        json.put("last_modified_date", resume.getLastModifiedDate());
        return json;
    }
}
