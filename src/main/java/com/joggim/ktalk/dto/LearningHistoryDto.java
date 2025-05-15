package com.joggim.ktalk.dto;

import com.joggim.ktalk.domain.LearningHistory;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningHistoryDto {
    private Long id;
    private String korean;
    private String translation;
    private boolean correct;
    private LocalDateTime studiedAt;

    public static LearningHistoryDto fromEntity(LearningHistory history) {
        return LearningHistoryDto.builder()
                .id(history.getId())
                .korean(history.getSentence().getKorean())
                .translation(history.getSentence().getTranslation())
                .correct(history.isCorrect())
                .studiedAt(history.getStudiedAt())
                .build();
    }
}