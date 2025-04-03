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
public class UserMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom; // 채팅방 ID

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 사용자가 입력한 텍스트

    private String userAudio; // 사용자가 녹음한 음성 URL

    private String correctAudio; // 올바른 바름 음성 URL

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "userMessage", cascade = CascadeType.ALL, orphanRemoval = true)
    private GrammarFeedback grammarFeedback; // 문법 피드백

    @OneToOne(mappedBy = "userMessage", cascade = CascadeType.ALL, orphanRemoval = true)
    private PronunciationFeedback pronunciationFeedback; // 발음 피드백

    public UserMessage(ChatRoom chatRoom, String content, String userAudio) {
        this.chatRoom = chatRoom;
        this.content = content;
        this.userAudio = userAudio;
        this.createdAt = LocalDateTime.now();
    }
}