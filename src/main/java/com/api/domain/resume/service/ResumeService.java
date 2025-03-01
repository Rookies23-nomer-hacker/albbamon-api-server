package com.api.domain.resume.service;

import com.api.domain.resume.entity.Resume;
import com.api.domain.resume.repository.ResumeRepository;
import com.api.domain.resume.repository.Resume_userRepository;
import com.api.domain.resume.request.ResumeListDto;
import com.api.domain.resume.request.ResumeRequestDto;
import com.api.domain.user.entity.User;
import com.api.domain.user.repository.UserRepository;
import com.api.global.error.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import static com.api.domain.resume.error.RecruitmentErrorCode.RESUME_NOT_FOUND;
import static com.api.domain.user.error.UserErrorCode.USER_NOT_FOUND;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.api.global.common.util.XorDecryptUtil;


@RequiredArgsConstructor
@Service
public class ResumeService {
	
	private final ResumeRepository resumeRepository;
	private final Resume_userRepository resumeUserRepository;
	private final UserRepository userRepository;
	
	@Value("${spring.datasource.encryption-key}")
  	private String encryptionKey;
	
    public void createResume(ResumeRequestDto resumerequestDto) {
    	
    	User user = userRepository.findUserById(resumerequestDto.user_id()).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    	Resume resume = Resume.createResume(user, resumerequestDto);
    	resumeRepository.save(resume);
    }
    
    public String duplicated(Long userId) {
    	Resume resume = resumeRepository.findResumeByUserId(userId).orElse(null);
    	if(resume==null) {
    		return "중복아님";
    	}else {
    		return "중복";
    	}
    }
    
    public Map<String, Object> getUserById(Long userId) {
    	User user = resumeUserRepository.findById(userId).orElse(null);
        Map<String,Object> json = new HashMap<>();
        json.put("email", XorDecryptUtil.xorDecrypt(user.getEmail(),encryptionKey));
        json.put("name", XorDecryptUtil.xorDecrypt(user.getName(),encryptionKey));
        json.put("phone", XorDecryptUtil.xorDecrypt(user.getPhone(),encryptionKey));
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
        	json.put("personal", XorDecryptUtil.xorDecrypt(resume.getPersonal(), encryptionKey));
        	json.put("work_place_region", XorDecryptUtil.xorDecrypt(resume.getWork_place_region(),encryptionKey));
        	json.put("industry_occupation", XorDecryptUtil.xorDecrypt(resume.getIndustry_occupation(),encryptionKey));
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
        	json.put("school",XorDecryptUtil.xorDecrypt(resume.getSchool(),encryptionKey));
        	json.put("status",XorDecryptUtil.xorDecrypt(resume.getStatus(),encryptionKey));
        	json.put("personal",XorDecryptUtil.xorDecrypt(resume.getPersonal(),encryptionKey));
        	json.put("work_place_region", XorDecryptUtil.xorDecrypt(resume.getWork_place_region(),encryptionKey));
        	json.put("work_place_city", XorDecryptUtil.xorDecrypt(resume.getWork_place_city(),encryptionKey));
        	json.put("industry_occupation", XorDecryptUtil.xorDecrypt(resume.getIndustry_occupation(),encryptionKey));
        	json.put("employmentType", XorDecryptUtil.xorDecrypt(resume.getEmploymentType(),encryptionKey));
        	json.put("working_period", XorDecryptUtil.xorDecrypt(resume.getWorking_period(),encryptionKey));
        	json.put("working_day", XorDecryptUtil.xorDecrypt(resume.getWorking_day(),encryptionKey));
        	json.put("introduction", XorDecryptUtil.xorDecrypt(resume.getIntroduction(),encryptionKey));
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

	public Map<String,Object> getResumeDetail(Long userId) {
		Resume resume = resumeRepository.findResumeByUserId(userId).orElseThrow(() -> new EntityNotFoundException(RESUME_NOT_FOUND));
		Map<String,Object> json = new HashMap<>();
		if(resume!=null) {
			json.put("user_id", resume.getUser().getId());
			json.put("resume_id", resume.getId());
			json.put("school",XorDecryptUtil.xorDecrypt(resume.getSchool(),encryptionKey));
			json.put("status",XorDecryptUtil.xorDecrypt(resume.getStatus(),encryptionKey));
			json.put("personal",XorDecryptUtil.xorDecrypt(resume.getPersonal(),encryptionKey));
			json.put("work_place_region", XorDecryptUtil.xorDecrypt(resume.getWork_place_region(),encryptionKey));
			json.put("work_place_city", XorDecryptUtil.xorDecrypt(resume.getWork_place_city(),encryptionKey));
			json.put("industry_occupation", XorDecryptUtil.xorDecrypt(resume.getIndustry_occupation(),encryptionKey));
			json.put("employmentType", XorDecryptUtil.xorDecrypt(resume.getEmploymentType(),encryptionKey));
			json.put("working_period", XorDecryptUtil.xorDecrypt(resume.getWorking_period(),encryptionKey));
			json.put("working_day", XorDecryptUtil.xorDecrypt(resume.getWorking_day(),encryptionKey));
			json.put("introduction", XorDecryptUtil.xorDecrypt(resume.getIntroduction(),encryptionKey));
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

	public Boolean checkResumeExists(Long userId) {
		return resumeRepository.existsByUserId(userId);
	}
	
    // 이력서 전체 조회
    public List<ResumeListDto> getAllResumes() {
        List<Resume> resumes = resumeRepository.findAll();
        
        // Resume -> ResumeList DTO 변환
        return resumes.stream()
                .map(resume -> new ResumeListDto(
                        resume.getId(),
                        resume.getSchool(),
                        resume.getStatus(),
                        resume.getPersonal(),
                        resume.getWork_place_region(),
                        resume.getWork_place_city(),
                        resume.getIndustry_occupation(),
                        resume.getEmploymentType(),
                        resume.getWorking_period(),
                        resume.getWorking_day(),
                        resume.getIntroduction(),
                        resume.getPortfolioname(),
                        resume.getPortfoliourl(),
                        resume.getResume_imgurl(),
                        resume.getResume_imgname()))
                .collect(Collectors.toList());
    }

	public String deleteResumeByUserId(Long userId) {
		Resume resume = resumeRepository.findResumeByUserId(userId).orElse(null);
		if(!Objects.isNull(resume)) {
			resumeRepository.delete(resume);
			return "삭제완료";
		}
		return "이력서가 없습니다";
	}
}
