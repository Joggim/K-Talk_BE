package com.joggim.ktalk.dto;

import com.joggim.ktalk.domain.BotMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BotMessageDto {
    private String type = "received"; // 항상 "received"로 설정
    private String korean;
    private String translation;
    private String modelAudioUrl;

    public BotMessageDto(String korean, String translation, String modelAudioUrl) {
        this.korean = korean;
        this.translation = translation;
        this.modelAudioUrl = modelAudioUrl;
    }

    public static BotMessageDto of(BotMessage botMessage) {
        return new BotMessageDto(
                botMessage.getContent(),
                botMessage.getTranslation(),
                botMessage.getModelAudioUrl()
        );
    }

}
