package com.ranjit.todo.todo.controllers;

import com.ranjit.todo.todo.dtos.ChatMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Logger _logger = LoggerFactory.getLogger(ChatController.class);
    public ChatController(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;

    }

    @MessageMapping("/chat")
    public void sendMessage(@RequestBody ChatMessageDTO message){
        _logger.info("Sending message: " + message.toString());
        simpMessagingTemplate.convertAndSend("/topic/messages/" + message.getReceiverId(), message);
    }
}
