package com.joggim.ktalk.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class GrammarFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_message_id", nullable = false)
    private UserMessage userMessage; // 연결된 피드백

    @Column(nullable = false, columnDefinition = "TEXT")
    private String suggestion; // 수정된 문장

    @Column(nullable = false, columnDefinition = "TEXT")
    private String explanation; // 문법 피드백 설명

    public GrammarFeedback(UserMessage userMessage, String suggestion, String explanation) {
        this.userMessage = userMessage;
        this.suggestion = suggestion;
        this.explanation = explanation;
    }
}
