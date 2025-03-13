package com.api.domain.admin.service;

import com.api.domain.admin.dto.request.AdminSignInRequestDto;
import com.api.domain.admin.dto.request.AdminSignUpRequestDto;
import com.api.domain.admin.dto.response.AdminSignInResponseDto;
import com.api.domain.admin.entity.Admin;
import com.api.domain.admin.repository.AdminRepository;
import com.api.global.common.util.EncoderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.api.global.common.util.XorDecryptUtil.xorDecrypt;
import static com.api.global.common.util.XorEncryptUtil.xorEncrypt;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final EncoderUtil encoderUtil;

    @Value("${spring.datasource.encryption-key}")
    private String encryptionKey;

    public void signUp(AdminSignUpRequestDto requestDto) {
        String encodedPassword = encoderUtil.encrypt(requestDto.password());
        System.out.println("******************************************* " + encodedPassword);
        System.out.println("------------------------------------------- " + xorEncrypt(requestDto.identity(), encryptionKey));
    }

    public AdminSignInResponseDto signIn(AdminSignInRequestDto requestDto) {
        Admin admin = adminRepository.findByIdentity(xorDecrypt(requestDto.identity(), encryptionKey)).orElse(null);
        if(Objects.isNull(admin)) {
            return AdminSignInResponseDto.of("관리자가 아닙니다");
        } else if(!validatePassword(admin.getPassword(), requestDto.password())) {
            return AdminSignInResponseDto.of("비밀번호가 일치하지 않습니다");
        }
        return AdminSignInResponseDto.of(xorDecrypt(admin.getIdentity(), encryptionKey));
    }

    private boolean validatePassword(String encryptedPassword, String rawPassword) {
        return encoderUtil.encrypt(rawPassword).equals(encryptedPassword);
    }
}
