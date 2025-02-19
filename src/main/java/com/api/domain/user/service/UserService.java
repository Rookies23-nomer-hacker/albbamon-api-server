package com.api.domain.user.service;

import com.api.domain.user.dto.request.CreateUserRequestDto;
import com.api.domain.user.dto.request.SignInRequestDto;
import com.api.domain.user.entity.User;
import com.api.domain.user.repository.UserRepository;
import com.api.global.common.util.EncoderUtil;
import com.api.global.error.exception.ConflictException;
import com.api.global.error.exception.EntityNotFoundException;
import com.api.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.api.domain.user.error.UserErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
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

    private void validatePassword(String encodedPassword, String rawPassword) {
        if(!encodedPassword.equals(encoderUtil.encrypt(rawPassword))) {
            throw new InvalidValueException(PASSWORD_INCORRECT);
        }
    }
}
