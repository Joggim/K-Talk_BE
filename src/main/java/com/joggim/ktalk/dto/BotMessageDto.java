package com.joggim.ktalk.dto;

import com.joggim.ktalk.domain.BotMessage;
import com.joggim.ktalk.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

public class BotMessageDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Save {
        private String content;
        private String translation;
        private String modelAudioUrl;

        public BotMessage toEntity(ChatRoom chatRoom) {
            return new BotMessage(chatRoom, content, translation, modelAudioUrl);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private UUID id;
        private String type = "received"; // 항상 "received"
        private String content;
        private String translation;
        private String modelAudioUrl;

        public Response(BotMessage entity) {
            this.id = entity.getId();
            this.content = entity.getContent();
            this.translation = entity.getTranslation();
            this.modelAudioUrl = entity.getModelAudioUrl();
        }
    }

}
