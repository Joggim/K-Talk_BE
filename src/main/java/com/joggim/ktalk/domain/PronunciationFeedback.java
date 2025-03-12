package com.joggim.ktalk.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PronunciationFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_message_id", nullable = false)
    private UserMessage userMessage; // 연결된 피드백

    @Column(nullable = false, columnDefinition = "TEXT")
    private String pronunciationErrors; // JSON 문자열로 저장 (예: [{"index":16, "char":"어"}])

    @Column(columnDefinition = "TEXT")
    private String explanation;

    public PronunciationFeedback(UserMessage userMessage, String pronunciationErrors) {
        this.userMessage = userMessage;
        this.pronunciationErrors = pronunciationErrors;
    }
}
