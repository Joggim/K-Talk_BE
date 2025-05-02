package com.joggim.ktalk.service;

import com.joggim.ktalk.domain.PronunciationIssue;
import com.joggim.ktalk.dto.PronunciationIssueRecommendationDto;
import com.joggim.ktalk.repository.UserPronunciationIssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearningRecommendationService {

    @Autowired
    private UserPronunciationIssueRepository userIssueRepo;

    public List<PronunciationIssueRecommendationDto> getRecommendations(String userId) {
        return userIssueRepo.findByUserUserIdOrderByAccuracyAsc(userId).stream()
                .map(userIssue -> {
                    PronunciationIssue issue = userIssue.getIssue();
                    return PronunciationIssueRecommendationDto.from(
                            issue.getId(),
                            issue.getTitle(),
                            userIssue.getAccuracy(),
                            issue.getSentences()
                    );
                })
                .toList();
    }

}
