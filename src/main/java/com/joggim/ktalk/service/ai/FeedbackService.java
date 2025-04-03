package com.joggim.ktalk.service.ai;

import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.domain.Sentence;
import com.joggim.ktalk.dto.AudioRequestDto;
import com.joggim.ktalk.dto.FeedbackDto;
import com.joggim.ktalk.repository.SentenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FeedbackService {

    @Autowired
    private SentenceRepository sentenceRepository;

    public FeedbackDto getFeedback(AudioRequestDto audio, Long sentenceId) {

        Sentence sentence = sentenceRepository.findById(sentenceId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        String originalText = sentence.getKorean();

        // AI 서버 호출 (원본 텍스트와 오디오 전송)


        return new FeedbackDto();
    }
}
