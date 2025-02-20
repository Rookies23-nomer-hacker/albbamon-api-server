package com.api.domain.resume.service;

import com.api.domain.post.entity.Post;
import com.api.domain.resume.entity.Resume;
import com.api.domain.resume.repository.ResumeRepository;
import com.api.domain.resume.repository.Resume_userRepository;
import com.api.domain.resume.request.ResumeRequestDto;
import com.api.domain.resume.request.Resume_profileRequestDto;
import com.api.domain.user.entity.User;
import com.api.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
}
