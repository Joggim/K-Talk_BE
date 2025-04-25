package com.joggim.ktalk.repository;

import com.joggim.ktalk.domain.BotMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BotMessageRepository extends JpaRepository<BotMessage, UUID> {
    List<BotMessage> findByChatRoomId(Long chatRoomId);
}
