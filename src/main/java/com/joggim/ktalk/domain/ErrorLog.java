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

    @ManyToMany
    @JoinTable(
            name = "error_log_pronunciation_issue",
            joinColumns = @JoinColumn(name = "error_log_id"),
            inverseJoinColumns = @JoinColumn(name = "pronunciation_issue_id")
    )
    private List<PronunciationIssue> pronunciationIssues = new ArrayList<>();


}
