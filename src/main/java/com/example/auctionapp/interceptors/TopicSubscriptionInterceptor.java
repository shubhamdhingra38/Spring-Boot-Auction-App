package com.example.auctionapp.interceptors;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import java.security.Principal;
import java.util.List;

public class TopicSubscriptionInterceptor implements ChannelInterceptor {
    final String SEPARATOR = ":";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())
                || StompCommand.SEND.equals(headerAccessor.getCommand())) {
            final Principal userPrincipal = headerAccessor.getUser();
            if (!validateSubscriptionAndSend(userPrincipal, headerAccessor.getDestination())) {
                throw new MessagingException("No permission for this topic");
            }
        }
        return message;
    }

    private boolean validateSubscriptionAndSend(final Principal principal, final String destination) {
        if (principal.getName() == null) {
            return false;
        }

        final String[] paths = destination.split("/");
        final String topicName = paths[paths.length - 1];
        System.out.println("Principal: " + principal);
        System.out.println("Destination: " + destination);
        System.out.println("Topic Name: " + topicName);

        final List<String> userNames = List.of(topicName.split(":"));
        if (!userNames.contains(principal.getName())) {
            return false;
        }
        return true;
    }
}
