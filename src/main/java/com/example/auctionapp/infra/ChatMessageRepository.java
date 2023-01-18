package com.example.auctionapp.infra;

import com.example.auctionapp.domain.ChatMessage;
import com.example.auctionapp.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {

    // TODO: Offset pagination not working when user posts new messages as it can result in duplicates
    @Query("SELECT chatMessage FROM ChatMessage chatMessage WHERE (chatMessage.sentBy = ?1 AND chatMessage.sentTo = ?2) OR (" +
            "chatMessage.sentTo = ?1 AND chatMessage.sentBy = ?2) ORDER BY chatMessage.sentAt DESC")
    Page<ChatMessage> findChatMessageBySentByAndSentToOrderBySentAtDesc(User sentBy, User sentTo, Pageable pageable);
}
