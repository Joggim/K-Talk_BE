package com.joggim.ktalk.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.domain.ErrorLog;
import com.joggim.ktalk.domain.LearningHistory;
import com.joggim.ktalk.domain.Sentence;
import com.joggim.ktalk.domain.User;
import com.joggim.ktalk.dto.FeedbackDto;
import com.joggim.ktalk.dto.LearningHistoryDto;
import com.joggim.ktalk.repository.LearningHistoryRepository;
import com.joggim.ktalk.repository.SentenceRepository;
import com.joggim.ktalk.repository.UserRepository;
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

                ErrorLog errorLog = ErrorLog.builder()
                        .userText(feedback.getUserText())
                        .userIpa(feedback.getUserIpa())
                        .errors(mapper.writeValueAsString(feedback.getPronunciationErrors()))
                        .history(history)
                        .build();

                // 오류 유형 불러오고 저장하기

                history.setErrorLog(errorLog);
            } catch (Exception e) {
                history.getErrorLog().setErrors(null);
            }
        }

        historyRepository.save(history);
    }

    public List<LearningHistoryDto> getHistoryByUserId(String userId) {
        return historyRepository.findByUserUserIdOrderByStudiedAtDesc(userId).stream()
                .map(LearningHistoryDto::fromEntity)
                .toList();
    }
}
