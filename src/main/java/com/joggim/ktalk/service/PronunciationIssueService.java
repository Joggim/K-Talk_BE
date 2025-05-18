package com.joggim.ktalk.service;

import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.domain.UserPronunciationIssue;
import com.joggim.ktalk.dto.PronunciationIssueDto;
import com.joggim.ktalk.dto.SentenceDto;
import com.joggim.ktalk.repository.UserPronunciationIssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PronunciationIssueService {

    @Autowired
    private UserPronunciationIssueRepository userIssueRepo;

    @Autowired
    private SentenceService sentenceService;

    public List<PronunciationIssueDto.Summary> getRecommendations(String userId) {
        return userIssueRepo.findByUserUserIdOrderByAccuracyAsc(userId).stream()
                .map(PronunciationIssueDto.Summary::from)
                .toList();
    }

    public PronunciationIssueDto.Detail getRecommendationDetail(String userId, Long issueId) {
        UserPronunciationIssue userIssue = userIssueRepo.findByUserUserIdAndIssueId(userId, issueId);

        List<SentenceDto> sentenceDtos = sentenceService.getSentencesByIssue(issueId, userId);

        return PronunciationIssueDto.Detail.from(userIssue, sentenceDtos);
    }

}
