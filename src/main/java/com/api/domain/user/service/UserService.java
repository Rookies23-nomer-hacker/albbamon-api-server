package com.api.domain.user.service;

import static com.api.domain.user.error.UserErrorCode.PASSWORD_INCORRECT;
import static com.api.domain.user.error.UserErrorCode.SIGN_IN_REQUIRED;
import static com.api.domain.user.error.UserErrorCode.USER_CONFLICT;
import static com.api.domain.user.error.UserErrorCode.USER_NOT_FOUND;

import java.util.List;
import java.util.Objects;

import com.api.domain.resume.entity.Resume;
import com.api.domain.resume.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.user.dto.request.CreateUserRequestDto;
import com.api.domain.user.dto.request.SignInRequestDto;
import com.api.domain.user.dto.response.GetUserInfoResponseDto;
import com.api.domain.user.dto.response.UserFindResponseDto;
import com.api.domain.user.entity.User;
import com.api.domain.user.mapper.UserMapper;
import com.api.domain.user.repository.UserRepository;
import com.api.domain.user.vo.UserVo;
import com.api.global.common.util.EncoderUtil;
import com.api.global.common.util.XorDecryptUtil;
import com.api.global.common.util.XorEncryptUtil;
import com.api.global.error.exception.ConflictException;
import com.api.global.error.exception.EntityNotFoundException;
import com.api.global.error.exception.InvalidValueException;
import com.api.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final UserMapper userMapper;
    private final EncoderUtil encoderUtil;
    
	@Value("${spring.datasource.encryption-key}")
  	private String encryptionKey;

    public void createUser(CreateUserRequestDto requestDto) {
        checkAlreadyExistingUser(requestDto.email());
        String encodedPassword = encoderUtil.encrypt(requestDto.password());
        User user = User.createUser(requestDto, encodedPassword);
        userRepository.save(user);
    }

    private void checkAlreadyExistingUser(String email) {
        User user = userRepository.findByEmail(email);
        if(!Objects.isNull(user)) {
            throw new ConflictException(USER_CONFLICT);
        }
    }

    public UserVo signIn(SignInRequestDto requestDto) {
        User user = userRepository.findUserByEmail(XorEncryptUtil.xorEncrypt(requestDto.email(),encryptionKey)).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));       
        UserVo loginUserVo = UserVo.of(user, 
        		XorDecryptUtil.xorDecrypt(user.getName(),encryptionKey), 
        		XorDecryptUtil.xorDecrypt(user.getEmail(),encryptionKey),
        		XorDecryptUtil.xorDecrypt(user.getPhone(),encryptionKey),
        		user.getCeoNum(),
        		user.getItem()
        		);
        
        
        if(!validatePassword(user.getPassword(), requestDto.password())) {
        	user.increasePwChkNum();
        	userRepository.save(user);
        	return null;
        }
        return loginUserVo;
    }

    public void deleteUser(Long userId) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        User user = userRepository.findUserById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        Resume resume = resumeRepository.findResumeByUserId(userId).orElse(null);
        if(!Objects.isNull(resume)) {
            resumeRepository.delete(resume);
        }
        userRepository.delete(user);
    }

    private boolean validatePassword(String encodedPassword, String rawPassword) {
        return encodedPassword.equals(encoderUtil.encrypt(rawPassword));
    }

    public GetUserInfoResponseDto getUserInfo(Long userId) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        UserVo userVo = userRepository.findUserVoById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        UserVo user = new UserVo(
        		userVo.id(),
        		XorDecryptUtil.xorDecrypt(userVo.name(),encryptionKey),
        		XorDecryptUtil.xorDecrypt(userVo.email(),encryptionKey),
        		XorDecryptUtil.xorDecrypt(userVo.phone(),encryptionKey),
        		userVo.ceoNum(),
                userVo.company(),
        		userVo.item(),
        		userVo.lastModifiedDate()
        );
        return userMapper.toGetUserInfoResponseDto(user);
    }
    
  //아이디 찾기
    public List<UserFindResponseDto> findUserByNameAndPhone(String name, String phone) {
    	List<User> users = userRepository.findByNameAndPhone(name, phone);
		if (users.isEmpty()) {
	        throw new EntityNotFoundException(USER_NOT_FOUND);
	    }
        List<UserFindResponseDto> responseDtos = users.stream()
                .map(user -> UserFindResponseDto.builder()
                        .email(user.getEmail())  // 이메일 설정
                        .type("per")
                        .success(true)           // 성공 여부
                        .build())
                .toList(); 
        return responseDtos;
    }
    public List<UserFindResponseDto> findUserByNameAndCeoNum(String name, String ceoNum) {
        List<User> users = userRepository.findByNameAndCeoNum(name, ceoNum);
        if (users.isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        List<UserFindResponseDto> responseDtos = users.stream()
                .map(user -> UserFindResponseDto.builder()
                        .email(user.getEmail())  // 이메일 설정
                        .type("cor")
                        .success(true)           // 성공 여부
                        .build())
                .toList();
        return responseDtos;
    }
    @Transactional
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        // ✅ 1. userId로 사용자 조회 (Long 타입 사용)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        // ✅ 2. 현재 비밀번호 검증 (EncoderUtil 사용)
        if (!user.getPassword().equals(encoderUtil.encrypt(currentPassword))) {
            throw new InvalidValueException(PASSWORD_INCORRECT); // 현재 비밀번호 불일치 예외 발생
        }

        // ✅ 3. 새 비밀번호 설정 및 저장 (EncoderUtil로 암호화)
        user.updatePassword(encoderUtil.encrypt(newPassword));
        userRepository.save(user);

        return true; // 비밀번호 변경 성공
    }
    
}
