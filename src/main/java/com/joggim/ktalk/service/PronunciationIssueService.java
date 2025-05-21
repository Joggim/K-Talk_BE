package com.joggim.ktalk.service;

import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.domain.ErrorLogPronunciationIssue;
import com.joggim.ktalk.domain.PronunciationIssue;
import com.joggim.ktalk.domain.UserPronunciationIssue;
import com.joggim.ktalk.dto.ErrorLogDto;
import com.joggim.ktalk.dto.PronunciationIssueDto;
import com.joggim.ktalk.dto.SentenceDto;
import com.joggim.ktalk.repository.ErrorLogPronunciationIssueRepository;
import com.joggim.ktalk.repository.PronunciationIssueRepository;
import com.joggim.ktalk.repository.UserPronunciationIssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PronunciationIssueService {

    @Autowired
    private UserPronunciationIssueRepository userIssueRepo;

    @Autowired
    private PronunciationIssueRepository pronunciationIssueRepository;

    @Autowired
    private SentenceService sentenceService;

    @Autowired
    private ErrorLogPronunciationIssueRepository errorLogPronunciationIssueRepository;

    public List<PronunciationIssueDto.Summary> getRecommendations(String userId) {
        return userIssueRepo.findByUserUserIdOrderByAccuracyAsc(userId).stream()
                .map(PronunciationIssueDto.Summary::from)
                .toList();
    }

    public PronunciationIssueDto.Detail getRecommendationDetail(String userId, Long issueId) {
        UserPronunciationIssue userIssue = userIssueRepo.findByUserUserIdAndIssueId(userId, issueId);

        List<SentenceDto> sentenceDtos = sentenceService.getSentencesByIssue(issueId, userId);

        List<ErrorLogPronunciationIssue> mappings = errorLogPronunciationIssueRepository
                .findByIssueIdAndUserId(issueId, userId);

        List<ErrorLogDto.Response> errorLogDtos = mappings.stream()
                .map(m -> ErrorLogDto.Response.from(m.getErrorLog(), m.getErrorIndex()))
                .toList();

        int totalErrorCount = errorLogDtos.size();

        return PronunciationIssueDto.Detail.from(userIssue, sentenceDtos, errorLogDtos, totalErrorCount);
    }

    public ErrorLogDto getErrorLogsByIssue(Long issueId, String userId) {
        PronunciationIssue issue = pronunciationIssueRepository.findById(issueId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        List<ErrorLogDto.Response> responses = issue.getErrorLogPronunciationIssues().stream()
                .filter(mapping -> mapping.getErrorLog().getHistory().getUser().getUserId().equals(userId))
                .map(mapping -> ErrorLogDto.Response.from(mapping.getErrorLog(), mapping.getErrorIndex()))
                .toList();

        return new ErrorLogDto(responses.size(), responses);
    }

}
