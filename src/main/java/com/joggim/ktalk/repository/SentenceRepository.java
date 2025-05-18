package com.joggim.ktalk.repository;

import com.joggim.ktalk.domain.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SentenceRepository extends JpaRepository<Sentence, Long> {
    List<Sentence> findByTopicId(Long topicId);
    List<Sentence> findByIssueId(Long issueId);
}
