package com.joggim.ktalk.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BotMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 봇이 보낸 메시지

    private String translation; // 번역문

    private String modelAudioUrl; // 봇의 발음(TTS) URL

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public BotMessage(ChatRoom chatRoom, String content, String translation, String modelAudioUrl) {
        this.chatRoom = chatRoom;
        this.content = content;
        this.translation = translation;
        this.modelAudioUrl = modelAudioUrl;
        this.createdAt = LocalDateTime.now();
    }
}