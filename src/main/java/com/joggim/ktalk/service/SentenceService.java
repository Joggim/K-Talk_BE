package com.joggim.ktalk.service;

import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.domain.Sentence;
import com.joggim.ktalk.dto.SentenceDto;
import com.joggim.ktalk.repository.SentenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SentenceService {

    @Autowired
    private SentenceRepository sentenceRepository;

    public SentenceDto getSentenceById(Long sentenceId) {
        Sentence sentence = sentenceRepository.findById(sentenceId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        return SentenceDto.fromEntity(sentence);
    }

    public List<SentenceDto> getSentencesByTopic(Long topicId) {
        List<Sentence> sentences = sentenceRepository.findByTopicId(topicId);

        return sentences.stream()
                .map(SentenceDto::fromEntity)
                .collect(Collectors.toList());
    }
}
