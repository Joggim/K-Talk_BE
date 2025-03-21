package com.joggim.ktalk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BotMessageDto {
    private String type = "received"; // 항상 "received"로 설정
    private String korean;
    private String translation;
    private String audioUrl;

    public BotMessageDto(String korean, String translation, String audioUrl) {
        this.korean = korean;
        this.translation = translation;
        this.audioUrl = audioUrl;
    }
}
