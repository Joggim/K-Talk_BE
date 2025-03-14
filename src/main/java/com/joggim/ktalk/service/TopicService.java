package com.joggim.ktalk.service;

import com.joggim.ktalk.domain.Topic;
import com.joggim.ktalk.dto.TopicDto;
import com.joggim.ktalk.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    public List<TopicDto> getTopics() {
        List<Topic> topics = topicRepository.findAll();
        return topics.stream()
                .map(TopicDto::fromEntity)
                .collect(Collectors.toList());
    }

}
