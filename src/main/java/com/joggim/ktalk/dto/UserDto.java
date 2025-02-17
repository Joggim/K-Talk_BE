package com.joggim.ktalk.dto;

import com.joggim.ktalk.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userId;
    private String nickname;

    public static UserDto fromEntity(User user) {
        return new UserDto(user.getUserId(), user.getNickname());
    }
}

