package com.api.domain.resume.service;

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
    	Resume resume = resumeRepository.findResumeByUserId(resumerequestDto.user_id()).orElse(null);
    	if(resume==null) {
    		return "중복아님";
    	}else {
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
    
    public String delete(Long resumeId) {
    	resumeRepository.deleteById(resumeId);
    	return "삭제완료";
    }
    
    public Map<String, Object> getResumeUser_id(Long user_id) {
    	Resume resume = resumeRepository.findByuser_id(user_id);
        Map<String,Object> json = new HashMap<>();
        if(resume!=null) {
        	json.put("resume_id", resume.getId());
        	json.put("personal", resume.getPersonal());
        	json.put("work_place_region", resume.getWork_place_region());
        	json.put("industry_occupation", resume.getIndustry_occupation());
        	json.put("last_modified_date", resume.getLastModifiedDate());
        	return json;
        }else {
        	
        	return null;
        }
    }
    public Map<String, Object> getResume_id(Long resumeId) {
    	Resume resume = resumeRepository.findByid(resumeId);
        Map<String,Object> json = new HashMap<>();
        if(resume!=null) {
        	json.put("user_id", resume.getUser().getId());
        	json.put("resume_id", resume.getId());
        	json.put("school",resume.getSchool());
        	json.put("status",resume.getStatus());
        	json.put("personal",resume.getPersonal());
        	json.put("work_place_region", resume.getWork_place_region());
        	json.put("work_place_city", resume.getWork_place_city());
        	json.put("industry_occupation", resume.getIndustry_occupation());
        	json.put("employmentType", resume.getEmploymentType());
        	json.put("working_period", resume.getWorking_period());
        	json.put("working_day", resume.getWorking_day());
        	json.put("introduction", resume.getIntroduction());
        	json.put("portfoliourl", resume.getPortfoliourl());
        	json.put("portfolioname", resume.getPortfolioname());
        	json.put("last_modified_date", resume.getLastModifiedDate());
        	json.put("resume_imgurl", resume.getResume_imgurl());
        	json.put("resume_img_name", resume.getResume_imgname());
        	return json;
        }else {
        	
        	return null;
        }
    }
}
