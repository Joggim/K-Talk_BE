package com.joggim.ktalk.service;

import com.joggim.ktalk.domain.PronunciationIssue;
import com.joggim.ktalk.domain.Sentence;
import com.joggim.ktalk.dto.PronunciationIssueDto;
import com.joggim.ktalk.repository.UserPronunciationIssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LearningRecommendationService {

    @Autowired
    private UserPronunciationIssueRepository userIssueRepo;

    public List<PronunciationIssueDto.Summary> getRecommendations(String userId) {
        return userIssueRepo.findByUserUserIdOrderByAccuracyAsc(userId).stream()
                .map(PronunciationIssueDto.Summary::from)
                .toList();
    }

    public PronunciationIssueDto.Detail getRecommendationDetail(String userId, Long issueId) {
        return PronunciationIssueDto.Detail.from(userIssueRepo.findByUserUserIdAndIssueId(userId, issueId));
    }

}
