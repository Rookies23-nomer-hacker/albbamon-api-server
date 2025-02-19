package com.api.domain.user.service;

import com.api.domain.user.dto.request.CreateUserRequestDto;
import com.api.domain.user.dto.request.SignInRequestDto;
import com.api.domain.user.dto.response.GetUserInfoResponseDto;
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

import java.util.Objects;

import static com.api.domain.user.error.UserErrorCode.*;

@RequiredArgsConstructor
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
}
