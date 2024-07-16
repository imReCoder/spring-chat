package com.ranjit.todo.todo.controllers;

import com.ranjit.todo.todo.dtos.ChatMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Logger _logger = LoggerFactory.getLogger(ChatController.class);
    public ChatController(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;

    }

    @MessageMapping("/chat")
    public void sendMessage(@RequestBody ChatMessageDTO message, Principal principal){
        _logger.info("Principal: " + principal.getName());
//        message.setSenderId(userDetails.getUsername());
        _logger.info("Received message: " + message.toString());
        String sendingTo = "/topic/"+"messages/"+message.getReceiverId();
        _logger.info("Forwarding to: " + sendingTo);
        simpMessagingTemplate.convertAndSend(sendingTo, message);
    }
}
