package com.joggim.ktalk.service;

import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.domain.User;
import com.joggim.ktalk.dto.UserDto;
import com.joggim.ktalk.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 사용자 조회
    @Transactional
    public UserDto getUserById(String userId) {
         User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserDto.fromEntity(user);
    }

    // 사용자 수정 (닉네임 수정)
    @Transactional
    public UserDto updateNickname(String userId, String nickname) {
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(nickname == null || nickname.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        target.setNickname(nickname);
        User updated = userRepository.save(target);
        return UserDto.fromEntity(updated);
    }

    // 사용자 삭제
    @Transactional
    public void deleteUser(String userId) {
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(target);
    }

}
