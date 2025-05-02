package com.joggim.ktalk.dto;

import com.joggim.ktalk.domain.Sentence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PronunciationIssueRecommendationDto {
    private Long id;
    private String title;
    private int accuracy;
    private List<SentenceDto> sentences;

    public static PronunciationIssueRecommendationDto from(
            Long id,
            String title,
            int accuracy,
            List<Sentence> sentenceList) {

        List<SentenceDto> sentenceDtos = sentenceList.stream()
                .map(SentenceDto::fromEntity)
                .toList();

        return PronunciationIssueRecommendationDto.builder()
                .id(id)
                .title(title)
                .accuracy(accuracy)
                .sentences(sentenceDtos)
                .build();
    }
}
