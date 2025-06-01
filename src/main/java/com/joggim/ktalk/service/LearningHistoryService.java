package com.joggim.ktalk.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.domain.*;
import com.joggim.ktalk.dto.ErrorAnalysisDto;
import com.joggim.ktalk.dto.FeedbackDto;
import com.joggim.ktalk.dto.LearningHistoryDto;
import com.joggim.ktalk.dto.RecommendedSentenceResponse;
import com.joggim.ktalk.repository.*;
import com.joggim.ktalk.service.ai.ErrorClassificationService;
import com.joggim.ktalk.service.ai.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LearningHistoryService {

    @Autowired
    private LearningHistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SentenceRepository sentenceRepository;

    @Autowired
    private ErrorLogPronunciationIssueRepository errorLogPronunciationIssueRepository;

    @Autowired
    private PronunciationIssueRepository pronunciationIssueRepository;

    @Autowired
    private ErrorLogRepository errorLogRepository;

    @Autowired
    private ErrorClassificationService errorClassificationService;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserPronunciationIssueRepository userPronunciationIssueRepository;

    public void saveLearningResult(String userId, FeedbackDto feedback) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Sentence sentence = sentenceRepository.findById(feedback.getSentenceId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        LearningHistory history = LearningHistory.builder()
                .user(user)
                .sentence(sentence)
                .correct(feedback.isPassed())
                .studiedAt(LocalDateTime.now())
                .build();

        historyRepository.save(history);

        if (!feedback.isPassed()) {
            try {
                ObjectMapper mapper = new ObjectMapper();

                // 1. ErrorLog 생성
                ErrorLog errorLog = ErrorLog.builder()
                        .userText(feedback.getUserText())
                        .userIpa(feedback.getUserIpa())
                        .errors(mapper.writeValueAsString(feedback.getPronunciationErrors()))
                        .history(history)
                        .build();

                errorLogRepository.save(errorLog);

                // 2. 오류 유형 불러오고 저장하기
                List<ErrorAnalysisDto.Response> classifiedErrors = errorClassificationService.classifyError(feedback.getErrorAnalysis());

                // 3. 각 에러 타입 저장 및 매핑
                classifiedErrors.forEach(res -> {
                    String errorType = res.getErrorType();

//                    if (errorType == null || errorType.startsWith("None")) {
//                        return;
//                    }

                    PronunciationIssue issue = pronunciationIssueRepository.findByTitle(errorType)
                            .orElseGet(() -> {
                                PronunciationIssue newIssue = PronunciationIssue.builder()
                                        .title(errorType)
                                        .build();
                                pronunciationIssueRepository.save(newIssue);

                                // AI 서버에 추천 문장 요청
                                recommendationService.fetchAndSaveRecommendedSentencesAsync(errorType, newIssue);

                                return newIssue;
                            });

                    boolean exists = userPronunciationIssueRepository.existsByUserAndIssue(user, issue);
                    if (!exists) {
                        UserPronunciationIssue userIssue = new UserPronunciationIssue();
                        userIssue.setUser(user);
                        userIssue.setIssue(issue);
                        userIssue.setAccuracy(0); // accuracy는 이후 계산 로직에서 갱신
                        userPronunciationIssueRepository.save(userIssue);
                    }

                    ErrorLogPronunciationIssue mapping = ErrorLogPronunciationIssue.builder()
                            .errorLog(errorLog)
                            .issue(issue)
                            .errorIndex(res.getErrorIndex())
                            .build();

                    errorLogPronunciationIssueRepository.save(mapping);
                });

            } catch (Exception e) {
                history.getErrorLog().setErrors(null);
            }
        }

    }

    public List<LearningHistoryDto> getHistoryByUserId(String userId) {
        return historyRepository.findByUserUserIdOrderByStudiedAtDesc(userId).stream()
                .map(LearningHistoryDto::fromEntity)
                .toList();
    }
}
