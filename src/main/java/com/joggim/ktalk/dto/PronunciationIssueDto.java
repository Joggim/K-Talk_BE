package com.joggim.ktalk.dto;

import com.joggim.ktalk.domain.Sentence;
import com.joggim.ktalk.domain.UserPronunciationIssue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

public class PronunciationIssueDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Summary {
        private Long id;
        private String title;
        private int accuracy;
        private List<SentenceDto> sentences;

        public static Summary from(UserPronunciationIssue issue) {
            return Summary.builder()
                    .id(issue.getIssue().getId())
                    .title(issue.getIssue().getTitle())
                    .accuracy(issue.getAccuracy())
                    .sentences(convertSentences(issue.getIssue().getSentences(), 2))
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Detail {
        private Long id;
        private String title;
        private int accuracy;
        private int totalErrorCount;
        private List<SentenceDto> recommendedSentences;
        private List<?> errorLogs; // 추후에 넣기

        public static Detail from(UserPronunciationIssue issue) {
            return Detail.builder()
                    .id(issue.getIssue().getId())
                    .title(issue.getIssue().getTitle())
                    .accuracy(issue.getAccuracy())
                    .totalErrorCount(0)
                    .recommendedSentences(convertSentences(issue.getIssue().getSentences(), 0))
                    .errorLogs(null)
                    .build();
        }
    }

    private static List<SentenceDto> convertSentences(List<Sentence> sentences, int limit) {
        Stream<Sentence> stream = sentences.stream();
        if (limit > 0) {
            stream = stream.limit(limit);
        }
        return stream
                .map(sentence -> SentenceDto.fromEntity(sentence, null))
                .toList();
    }
}
