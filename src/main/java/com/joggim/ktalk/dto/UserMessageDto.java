package com.joggim.ktalk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joggim.ktalk.domain.ChatRoom;
import com.joggim.ktalk.domain.UserMessage;
import com.joggim.ktalk.domain.GrammarFeedback;
import com.joggim.ktalk.domain.PronunciationFeedback;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMessageDto {

    // AI 응답 → 저장용 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Save {
        private String content;
        private String userAudioUrl;
        private String modelAudioUrl;
        private ChatFeedbackDto feedback;

        public UserMessage toEntity(ChatRoom chatRoom) {
            UserMessage message = new UserMessage(chatRoom, content, userAudioUrl, modelAudioUrl);
            try {
                ObjectMapper mapper = new ObjectMapper();
                if (feedback != null) {
                    message.setFeedback(mapper.writeValueAsString(feedback));
                }
            } catch (Exception e) {
                message.setFeedback(null);
            }
            return message;
        }
    }

    // 저장된 메시지 → 조회 응답용 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private UUID id;
        private String type = "sent";
        private String content;
        private String userAudioUrl;
        private String modelAudioUrl;
        private ChatFeedbackDto feedback;

        public Response(UserMessage entity) {
            this.id = entity.getId();
            this.content = entity.getContent();
            this.userAudioUrl = entity.getUserAudioUrl();
            this.modelAudioUrl = entity.getModelAudioUrl();

            if (entity.getFeedback() != null) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    this.feedback = mapper.readValue(entity.getFeedback(), ChatFeedbackDto.class);
                } catch (Exception e) {
                    this.feedback = null;
                }
            }
        }
    }

    // 공통 구조
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChatFeedbackDto {
        private GrammarFeedbackDto grammar;
        private PronunciationFeedbackDto pronunciation;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class GrammarFeedbackDto {
        private String suggestion;
        private String explanation;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PronunciationFeedbackDto {
        private JsonNode pronunciationErrors;
    }
}
