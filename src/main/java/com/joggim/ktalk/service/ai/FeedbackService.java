package com.joggim.ktalk.service.ai;

import com.joggim.ktalk.dto.FeedbackDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FeedbackService {

    public FeedbackDto getFeedback(MultipartFile audioFile, String text) {

        // AI 서버 호출

        return new FeedbackDto();
    }
}
