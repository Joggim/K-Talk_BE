package com.joggim.ktalk.repository;

import com.joggim.ktalk.domain.LearningHistory;
import com.joggim.ktalk.domain.Sentence;
import com.joggim.ktalk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LearningHistoryRepository extends JpaRepository<LearningHistory, Long> {
    List<LearningHistory> findByUserUserIdOrderByStudiedAtDesc(String userId);

    List<LearningHistory> findByUserAndSentence(User user, Sentence sentence);

    List<LearningHistory> findByUserAndSentenceIn(User user, List<Sentence> sentences);


}