package com.joggim.ktalk.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joggim.ktalk.domain.ErrorLog;
import com.joggim.ktalk.domain.LearningHistory;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningHistoryDto {
    private Long id;
    private Long sentenceId;
    private String korean;
    private String translation;
    private boolean correct;
    private LocalDateTime studiedAt;
    private List<PronunciationError> pronunciationErrors;
    private List<String> errorTypes;

    public static LearningHistoryDto fromEntity(LearningHistory history) {
        List<PronunciationError> errors = null;
        List<String> errorTypes = null;

        ErrorLog errorLog = history.getErrorLog();

        if (errorLog != null && errorLog.getErrors() != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                errors = objectMapper.readValue(
                        errorLog.getErrors(),
                        new TypeReference<List<PronunciationError>>() {}
                );
            } catch (Exception e) {
                errors = null; // 또는 로깅
            }
        }

        if (errorLog != null && errorLog.getErrorLogPronunciationIssues() != null) {
            errorTypes = errorLog.getErrorLogPronunciationIssues().stream()
                    .map(mapping -> mapping.getIssue().getTitle())
                    .distinct()
                    .toList();
        }

        return LearningHistoryDto.builder()
                .id(history.getId())
                .sentenceId(history.getSentence().getId())
                .korean(history.getSentence().getKorean())
                .translation(history.getSentence().getTranslation())
                .correct(history.isCorrect())
                .studiedAt(history.getStudiedAt())
                .pronunciationErrors(errors)
                .errorTypes(errorTypes)
                .build();
    }
}
