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

import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
        if(user.getPwCheck()) {
    		return null;
    	}
        if(!validatePassword(user.getPassword(), requestDto.password())) {
        	user.increasePwChkNum();
        	userRepository.save(user);
        	return UserVo.of(user, 
            		XorDecryptUtil.xorDecrypt(user.getName(),encryptionKey), 
            		XorDecryptUtil.xorDecrypt(user.getEmail(),encryptionKey),
            		XorDecryptUtil.xorDecrypt(user.getPhone(),encryptionKey),
            		user.getCeoNum(),
            		user.getItem(),
            		user.getPwChkNum(),
            		user.getPwCheck()
            		);
        }
        user.unlockAccount();
        return UserVo.of(user, 
        		XorDecryptUtil.xorDecrypt(user.getName(),encryptionKey), 
        		XorDecryptUtil.xorDecrypt(user.getEmail(),encryptionKey),
        		XorDecryptUtil.xorDecrypt(user.getPhone(),encryptionKey),
        		user.getCeoNum(),
        		user.getItem(),
        		user.getPwChkNum(),
        		user.getPwCheck()
        		);
    }

    public UserVo autosignIn(String email) {

        System.out.println("base64 encode email : " + email);
        String decodedEmail = decodeBase64(email);

        User user = userRepository.findUserByEmail(XorEncryptUtil.xorEncrypt(decodedEmail,encryptionKey)).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));       
        UserVo loginUserVo = UserVo.of(user, 
        	                        XorDecryptUtil.xorDecrypt(user.getName(),encryptionKey), 
                                    XorDecryptUtil.xorDecrypt(user.getEmail(),encryptionKey),
                                    XorDecryptUtil.xorDecrypt(user.getPhone(),encryptionKey),
                                    user.getCeoNum(),
                                    user.getItem(),
                                    user.getPwChkNum(),
                            		user.getPwCheck()
                                    );
        return loginUserVo;
    }

    private String decodeBase64(String encodedEmail) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedEmail);
        return new String(decodedBytes, StandardCharsets.UTF_8);
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
        		userVo.lastModifiedDate(),
        		userVo.pwChkNum(),
        		userVo.pwCheck()
        );
        return userMapper.toGetUserInfoResponseDto(user);
    }
    
  //아이디 찾기
    public List<UserFindResponseDto> findUserByNameAndPhone(String name, String phone) {
    	List<User> users = userRepository.findByNameAndPhone(XorEncryptUtil.xorEncrypt(name,encryptionKey),XorEncryptUtil.xorEncrypt(phone,encryptionKey));
		if (users.isEmpty()) {
	        throw new EntityNotFoundException(USER_NOT_FOUND);
	    }
        List<UserFindResponseDto> responseDtos = users.stream()
                .map(user -> UserFindResponseDto.builder()
                        .email(XorDecryptUtil.xorDecrypt(user.getEmail(),encryptionKey))  // 이메일 설정
                        .type("per")
                        .success(true)           // 성공 여부
                        .build())
                .toList(); 
        return responseDtos;
    }
    public List<UserFindResponseDto> findUserByNameAndCeoNum(String name, String ceoNum) {
        List<User> users = userRepository.findByNameAndCeoNum(XorEncryptUtil.xorEncrypt(name,encryptionKey),ceoNum);
        if (users.isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        List<UserFindResponseDto> responseDtos = users.stream()
                .map(user -> UserFindResponseDto.builder()
                        .email(XorDecryptUtil.xorDecrypt(user.getEmail(),encryptionKey))  // 이메일 설정
                        .type("cor")
                        .success(true)           // 성공 여부
                        .build())
                .toList();
        return responseDtos;
    }
    @Transactional
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        if (!user.getPassword().equals(encoderUtil.encrypt(currentPassword))) {
            throw new InvalidValueException(PASSWORD_INCORRECT);
        }
        user.updatePassword(encoderUtil.encrypt(newPassword));
        userRepository.save(user);
        return true;
    }
    
}
