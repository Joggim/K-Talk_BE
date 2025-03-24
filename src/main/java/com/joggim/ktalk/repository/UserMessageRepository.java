package com.joggim.ktalk.repository;

import com.joggim.ktalk.domain.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
    List<UserMessage> findByChatRoomId(Long chatRoomId);
}