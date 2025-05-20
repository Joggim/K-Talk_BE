package com.joggim.ktalk.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joggim.ktalk.domain.ErrorLog;
import com.joggim.ktalk.domain.LearningHistory;
import com.joggim.ktalk.domain.Sentence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ErrorLogDto {
    private int totalErrorLogCount;
    private List<Response> errorLogs;

    @Data
    @Builder
    public static class Response {
        private Long id;
        private String translation;
        private String correctText;
        private String correctIpa;
        private String userText;
        private String userIpa;
        private ErrorInfo error;

        @Data
        @AllArgsConstructor
        public static class ErrorInfo {
            private String character;
            private int index;
        }

        public static Response from(ErrorLog log, int index) {
            LearningHistory history = log.getHistory();
            Sentence sentence = history.getSentence();

            List<PronunciationError> errors = new ArrayList<>();
            try {
                ObjectMapper mapper = new ObjectMapper();
                errors = mapper.readValue(log.getErrors(), new TypeReference<>() {});
            } catch (Exception e) {

            }

            PronunciationError matched = errors.stream()
                    .filter(err -> err.getIndex() == index)
                    .findFirst()
                    .orElse(new PronunciationError("", "", index));

            return Response.builder()
                    .id(log.getId())
                    .translation(sentence.getTranslation())
                    .correctText(sentence.getKorean())
                    .correctIpa(sentence.getIpa())
                    .userText(log.getUserText())
                    .userIpa(log.getUserIpa())
                    .error(new ErrorInfo(matched.getWrong(), index))
                    .build();
        }
    }
}
