package com.joggim.ktalk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joggim.ktalk.domain.UserMessage;
import com.joggim.ktalk.domain.GrammarFeedback;
import com.joggim.ktalk.domain.PronunciationFeedback;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMessageDto {

    private String type = "sent";  // 메시지 유형
    private String content;  // 메시지 내용
    private String userAudioUrl;  // 사용자가 보낸 오디오 URL
    private String modelAudioUrl;  // 모델의 오디오 URL
    private ChatFeedbackDto feedback; // 피드백

    public UserMessageDto(UserMessage userMessage) {
        this.content = userMessage.getContent();
        this.userAudioUrl = userMessage.getUserAudio();
        this.modelAudioUrl = userMessage.getCorrectAudio();

        if (userMessage.getGrammarFeedback() != null || userMessage.getPronunciationFeedback() != null) {
            this.feedback = new ChatFeedbackDto(userMessage.getGrammarFeedback(), userMessage.getPronunciationFeedback());
        } else {
            this.feedback = null; // grammar와 pronunciation 둘 다 null일 경우 feedback null
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ChatFeedbackDto {
        private GrammarFeedbackDto grammar;
        private PronunciationFeedbackDto pronunciation;

        public ChatFeedbackDto(GrammarFeedback grammarFeedback, PronunciationFeedback pronunciationFeedback) {
            if (grammarFeedback != null) {
                this.grammar = new GrammarFeedbackDto(grammarFeedback);
            }
            if (pronunciationFeedback != null) {
                this.pronunciation = new PronunciationFeedbackDto(pronunciationFeedback);
            }
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class GrammarFeedbackDto {
        private String suggestion;
        private String explanation;

        public GrammarFeedbackDto(GrammarFeedback grammarFeedback) {
            this.suggestion = grammarFeedback.getSuggestion();
            this.explanation = grammarFeedback.getExplanation();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PronunciationFeedbackDto {
        private JsonNode pronunciationErrors;
        private String explanation;

        public PronunciationFeedbackDto(PronunciationFeedback pronunciationFeedback) {
            this.explanation = pronunciationFeedback.getExplanation();
            try {
                // 문자열로 저장된 pronunciationErrors를 JSON 형식으로 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                this.pronunciationErrors = objectMapper.readTree(pronunciationFeedback.getPronunciationErrors());
            } catch (Exception e) {
                this.pronunciationErrors = null; // 파싱 중 오류 발생 시 null 처리
            }
        }
    }
}
