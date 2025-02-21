package com.api.domain.apply.service;

import com.api.domain.apply.dto.response.GetApplyListResponseDto;
import com.api.domain.apply.repository.ApplyRepository;
import com.api.domain.apply.vo.ApplyVo;
import com.api.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.api.domain.user.error.UserErrorCode.SIGN_IN_REQUIRED;

@RequiredArgsConstructor
@Service
public class ApplyService {
    private final ApplyRepository applyRepository;

    public GetApplyListResponseDto getMyApplyList(Long userId) {
        if(userId == null) throw new UnauthorizedException(SIGN_IN_REQUIRED);
        List<ApplyVo> applyVoList = applyRepository.findApplyVoByUserId(userId);
        return GetApplyListResponseDto.of(applyVoList);
    }
}
