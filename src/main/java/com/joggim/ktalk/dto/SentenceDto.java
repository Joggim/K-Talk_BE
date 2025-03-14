package com.joggim.ktalk.dto;

import com.joggim.ktalk.domain.Sentence;
import lombok.*;

import java.util.List;

public class SentenceDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SentenceDetail {
        private Long id;
        private String korean;
        private String translation;
        private String audioUrl;

        public static SentenceDetail fromEntity(Sentence sentence) {
            return SentenceDetail.builder()
                    .id(sentence.getId())
                    .korean(sentence.getKorean())
                    .translation(sentence.getTranslation())
                    .audioUrl(sentence.getAudioUrl())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SentenceSummary {
        private Long id;
        private String korean;
        private String translation;

        public static SentenceSummary fromEntity(Sentence sentence) {
            return SentenceSummary.builder()
                    .id(sentence.getId())
                    .korean(sentence.getKorean())
                    .translation(sentence.getTranslation())
                    .build();
        }
    }

}
