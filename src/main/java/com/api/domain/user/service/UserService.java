package com.api.domain.user.service;

import static com.api.domain.user.error.UserErrorCode.PASSWORD_INCORRECT;
import static com.api.domain.user.error.UserErrorCode.SIGN_IN_REQUIRED;
import static com.api.domain.user.error.UserErrorCode.USER_CONFLICT;
import static com.api.domain.user.error.UserErrorCode.USER_NOT_FOUND;

import java.util.List;
import java.util.Objects;

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
import com.api.global.error.exception.ConflictException;
import com.api.global.error.exception.EntityNotFoundException;
import com.api.global.error.exception.InvalidValueException;
import com.api.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.api.domain.user.error.UserErrorCode.*;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EncoderUtil encoderUtil;

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

    public Long signIn(SignInRequestDto requestDto) {
        User user = userRepository.findUserByEmail(requestDto.email()).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        validatePassword(user.getPassword(), requestDto.password());
        return user.getId();
    }

    public void deleteUser(Long userId) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        User user = userRepository.findUserById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        userRepository.delete(user);
    }

    private void validatePassword(String encodedPassword, String rawPassword) {
        if(!encodedPassword.equals(encoderUtil.encrypt(rawPassword))) {
            throw new InvalidValueException(PASSWORD_INCORRECT);
        }
    }

    public GetUserInfoResponseDto getUserInfo(Long userId) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        UserVo userVo = userRepository.findUserVoById(userId).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        return userMapper.toGetUserInfoResponseDto(userVo);
    }
    
    //아이디 찾기
    public UserFindResponseDto findUserByNameAndPhone(String name, String phone) {
    	System.out.println("📌 API에서 검색할 name: " + name + ", phone: " + phone);  // 🔍 확인

    	List<User> user = userRepository.findByNameAndPhone(name, phone);
		if (user.isEmpty()) {
	        System.out.println("❌ DB에 해당 사용자가 존재하지 않음: " + name + ", " + phone);
	        throw new EntityNotFoundException(USER_NOT_FOUND);
	    }
        System.out.println("📌 DB에서 찾은 사용자: " + user);  // 🔍 확인
        // 🔍 데이터베이스에서 이름(name)과 전화번호(phone)로 사용자 찾기
 

        // 📨 찾은 사용자 정보를 Response DTO로 변환하여 반환
        return UserFindResponseDto.builder()
                 // 사용자의 이메일 정보
                .success(true) // 성공 여부 (사용자 존재하므로 true)
                .build();
    }
}
