package com.joggim.ktalk.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom; // 채팅방 ID

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 사용자가 입력한 텍스트

    private String userAudioUrl; // 사용자가 녹음한 음성 URL

    private String modelAudioUrl; // 올바른 바름 음성 URL

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "json")
    private String feedback;

    public UserMessage(ChatRoom chatRoom, String content, String userAudioUrl, String modelAudioUrl) {
        this.chatRoom = chatRoom;
        this.content = content;
        this.userAudioUrl = userAudioUrl;
        this.modelAudioUrl = modelAudioUrl;
        this.createdAt = LocalDateTime.now();
    }
}