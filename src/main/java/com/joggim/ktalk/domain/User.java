package com.joggim.ktalk.domain;

import com.joggim.ktalk.dto.UserDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String userId;

    @Column(nullable = false)
    private String nickname;

    public static User toEntity(UserDto dto) {
        return new User(dto.getUserId(), dto.getNickname());
    }

}
