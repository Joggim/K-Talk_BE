package com.joggim.ktalk.service;

import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.domain.LearningHistory;
import com.joggim.ktalk.domain.Sentence;
import com.joggim.ktalk.domain.User;
import com.joggim.ktalk.dto.SentenceDto;
import com.joggim.ktalk.repository.LearningHistoryRepository;
import com.joggim.ktalk.repository.SentenceRepository;
import com.joggim.ktalk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SentenceService {

    @Autowired
    private SentenceRepository sentenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LearningHistoryRepository learningHistoryRepository;

    public Sentence findById(Long sentenceId) {
        return sentenceRepository.findById(sentenceId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    public SentenceDto getSentenceById(Long sentenceId, String userId) {
        Sentence sentence = sentenceRepository.findById(sentenceId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<LearningHistory> histories = learningHistoryRepository.findByUserAndSentence(user, sentence);

        Boolean isPassed = null;
        if (!histories.isEmpty()) {
            isPassed = histories.stream().anyMatch(LearningHistory::isCorrect);
        }

        return SentenceDto.fromEntity(sentence, isPassed);
    }

    public List<SentenceDto> getSentencesByTopic(Long topicId, String userId) {
        List<Sentence> sentences = sentenceRepository.findByTopicId(topicId);
        return convertToSentenceDtosWithPassStatus(sentences, userId);
    }

    public List<SentenceDto> getSentencesByIssue(Long issueId, String userId) {
        List<Sentence> sentences = sentenceRepository.findByIssueId(issueId);
        return convertToSentenceDtosWithPassStatus(sentences, userId);
    }

    private List<SentenceDto> convertToSentenceDtosWithPassStatus(List<Sentence> sentences, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<LearningHistory> histories = learningHistoryRepository.findByUserAndSentenceIn(user, sentences);

        Map<Long, List<Boolean>> sentenceCorrectMap = histories.stream()
                .collect(Collectors.groupingBy(
                        history -> history.getSentence().getId(),
                        Collectors.mapping(LearningHistory::isCorrect, Collectors.toList())
                ));

        return sentences.stream()
                .map(sentence -> {
                    List<Boolean> correctList = sentenceCorrectMap.get(sentence.getId());
                    Boolean isPassed = null;
                    if (correctList != null) {
                        isPassed = correctList.contains(true);
                    }
                    return SentenceDto.fromEntity(sentence, isPassed);
                })
                .toList();
    }

    public Sentence updateAudioUrl(Long sentenceId, String audioUrl) {
        Sentence sentence = sentenceRepository.findById(sentenceId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        sentence.setAudioUrl(audioUrl);
        return sentenceRepository.save(sentence);
    }
}
