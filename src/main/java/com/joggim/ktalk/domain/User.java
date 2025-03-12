package com.joggim.ktalk.domain;

import com.joggim.ktalk.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> chatRooms;

    public User (String userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public static User toEntity(UserDto dto) {
        return User.builder()
                .userId(dto.getUserId())
                .nickname(dto.getNickname())
                .build();
    }

}
