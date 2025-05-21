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
            List<SentenceDto> previewSentences = issue.getIssue().getSentences().stream()
                    .limit(2)
                    .map(sentence -> SentenceDto.fromEntity(sentence, null))
                    .toList();

            return Summary.builder()
                    .id(issue.getIssue().getId())
                    .title(issue.getIssue().getTitle())
                    .accuracy(issue.getAccuracy())
                    .sentences(previewSentences)
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
        private int totalErrorLogCount;
        private List<SentenceDto> recommendedSentences;
        private List<?> errorLogs; // 추후에 넣기

        public static Detail from(UserPronunciationIssue userIssue,
                                  List<SentenceDto> sentenceDtos,
                                  List<ErrorLogDto.Response> errorLogs,
                                  int totalErrorLogCount) {
            return Detail.builder()
                    .id(userIssue.getIssue().getId())
                    .title(userIssue.getIssue().getTitle())
                    .accuracy(userIssue.getAccuracy())
                    .totalErrorLogCount(0) // 추후 계산 로직 반영
                    .recommendedSentences(sentenceDtos)
                    .errorLogs(null) // 추후 주입
                    .build();
        }
    }
}
