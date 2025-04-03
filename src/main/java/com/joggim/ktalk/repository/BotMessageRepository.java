package com.joggim.ktalk.repository;

import com.joggim.ktalk.domain.BotMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BotMessageRepository extends JpaRepository<BotMessage, Long> {
    List<BotMessage> findByChatRoomId(Long chatRoomId);
}
