package com.joggim.ktalk.repository;

import com.joggim.ktalk.domain.PronunciationIssue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PronunciationIssueRepository extends JpaRepository<PronunciationIssue, Long> {

    Optional<PronunciationIssue> findByTitle(String title);
}
