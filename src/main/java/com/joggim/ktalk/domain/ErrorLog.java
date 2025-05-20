package com.joggim.ktalk.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String userText;

    @Column(columnDefinition = "text")
    private String userIpa;

    @Column(columnDefinition = "json")
    private String errors;

    @OneToOne
    @JoinColumn(name = "history_id", nullable = false)
    private LearningHistory history;

    @OneToMany(mappedBy = "errorLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ErrorLogPronunciationIssue> errorLogPronunciationIssues = new ArrayList<>();

}
