package com.joggim.ktalk.dto;

import com.joggim.ktalk.domain.Topic;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicDto {

    private Long id;

    private String title;

    private String description;

    public static TopicDto fromEntity(Topic topic) {
        return TopicDto.builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .description(topic.getDescription())
                .build();
    }

}
