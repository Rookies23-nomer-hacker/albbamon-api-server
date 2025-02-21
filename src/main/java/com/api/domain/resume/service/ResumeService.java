package com.api.domain.resume.service;

import com.api.domain.recruitment.entity.Recruitment;
import com.api.domain.resume.entity.Resume;
import com.api.domain.resume.repository.ResumeRepository;
import com.api.domain.resume.repository.Resume_userRepository;
import com.api.domain.resume.request.ResumeRequestDto;
import com.api.domain.user.entity.User;
import com.api.domain.user.repository.UserRepository;
import com.api.global.error.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import static com.api.domain.user.error.UserErrorCode.USER_NOT_FOUND;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ResumeService {
	
	private final ResumeRepository resumeRepository;
	private final Resume_userRepository resumeUserRepository;
	private final UserRepository userRepository;
	
    public void createResume(ResumeRequestDto resumerequestDto) {
    	
    	User user = userRepository.findUserById(resumerequestDto.user_id()).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    	Resume resume = Resume.createResume(user, resumerequestDto);
    	resumeRepository.save(resume);
    }
    
    public String duplicated(ResumeRequestDto resumerequestDto) {
    	User user = userRepository.findUserById(resumerequestDto.user_id()).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    	Resume resume = Resume.duplicated(user, resumerequestDto);
    	if(resume.getId()!=null) {
    		System.out.println("중복아님");
    		return "중복아님";
    	}else {
    		System.out.println("중복");
    		return "중복";
    	}
    	
    }
    
    public Map<String, Object> getUserById(Long userId) {
    	User user = resumeUserRepository.findById(userId).orElse(null);
        Map<String,Object> json = new HashMap<>();
        json.put("email", user.getEmail());
        json.put("name", user.getName());
        json.put("phone", user.getPhone());
        return json;
    }
    
    public Map<String, Object> getresumeUser_id(Long user_id) {
    	Resume resume = resumeRepository.findByuser_id(user_id);
        Map<String,Object> json = new HashMap<>();
        if(resume!=null) {
        	json.put("personal", resume.getPersonal());
        	json.put("work_place_region", resume.getWork_place_region());
        	json.put("industry_occupation", resume.getIndustry_occupation());
        	json.put("last_modified_date", resume.getLastModifiedDate());
        	return json;
        }else {
        	
        	return null;
        }
    }
}//
