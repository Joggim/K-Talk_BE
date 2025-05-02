package com.joggim.ktalk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.joggim.ktalk.domain.Sentence;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SentenceDto {

    private Long id;
    private String korean;
    private String translation;
    private String audioUrl;

    public static SentenceDto fromEntity(Sentence sentence) {
        return SentenceDto.builder()
                .id(sentence.getId())
                .korean(sentence.getKorean())
                .translation(sentence.getTranslation())
                .audioUrl(sentence.getAudioUrl())
                .build();
    }

}
