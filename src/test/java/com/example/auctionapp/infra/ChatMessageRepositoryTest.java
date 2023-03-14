package com.example.auctionapp.infra;

import com.example.auctionapp.domain.ChatMessage;
import com.example.auctionapp.domain.User;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatMessageRepositoryTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    public void testFindChatMessageBySentByAndSentToOrderBySentAtDesc() {
        // Given
        final User user1 = TestUtils.createUser(userRepository);
        final User user2 = TestUtils.createUser(userRepository);

        final ChatMessage chatMessage1 = new ChatMessage();
        chatMessage1.setSentTo(user2);
        chatMessage1.setSentBy(user1);
        chatMessage1.setContent("Message1");


        final ChatMessage chatMessage2 = new ChatMessage();
        chatMessage2.setSentTo(user2);
        chatMessage2.setSentBy(user1);
        chatMessage2.setContent("Message2");

        chatMessageRepository.save(chatMessage1);
        chatMessageRepository.save(chatMessage2);

        // When
        final Page<ChatMessage> chatMessagesPage =
                chatMessageRepository.findChatMessageBySentByAndSentToOrderBySentAtDesc(user1, user2, PageRequest.of(0, 10));

        // Then
        Assertions.assertEquals(chatMessagesPage.getTotalElements(), 2);

        final List<ChatMessage> chatMessages = chatMessagesPage.stream().toList();
        Assertions.assertEquals(user1.getId(), chatMessages.get(0).getSentBy().getId());
        Assertions.assertEquals(user2.getId(), chatMessages.get(0).getSentTo().getId());
        Assertions.assertEquals("Message2", chatMessages.get(0).getContent());
    }

    @Test
    public void testGetDistinctUsersBySentByOrSentAt() {
        // Given
        final User user1 = TestUtils.createUser(userRepository);
        final User user2 = TestUtils.createUser(userRepository);

        final ChatMessage chatMessage1 = new ChatMessage();
        chatMessage1.setSentTo(user2);
        chatMessage1.setSentBy(user1);
        chatMessage1.setContent("Message1");


        final ChatMessage chatMessage2 = new ChatMessage();
        chatMessage2.setSentTo(user1);
        chatMessage2.setSentBy(user2);
        chatMessage2.setContent("Message2");

        chatMessageRepository.save(chatMessage1);
        chatMessageRepository.save(chatMessage2);

        // When
        final List<Long> distinctUsers = chatMessageRepository.getDistinctUsersBySentByOrSentAt(user1.getId());

        // Then
        Assertions.assertEquals(distinctUsers.size(), 1);
    }
}
