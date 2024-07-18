package com.ranjit.todo.todo.controllers;

import com.ranjit.todo.todo.dtos.ChatMessageDTO;
import com.ranjit.todo.todo.dtos.MessageUpdateDTO;
import com.ranjit.todo.todo.entities.MessageEntity;
import com.ranjit.todo.todo.enums.MessageStatusEnum;
import com.ranjit.todo.todo.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;

@Controller
public class SocketController {


    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;

    private final Logger _logger = LoggerFactory.getLogger(SocketController.class);
    public SocketController(SimpMessagingTemplate simpMessagingTemplate, MessageService messageService){
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/chat/message.send")
    public void sendMessage(@RequestBody ChatMessageDTO message, Principal principal){
        _logger.info("Message received: " + message.toString());
        message.setSenderId(principal.getName());
        MessageEntity savedMessage = this.messageService.saveMessage(message);
        String sendingTo = "/topic/"+"message.receiver/"+message.getReceiverId();
        String sendMessageUpdateTo = "/topic/"+"message.updates.sent/"+message.getSenderId();
//      create a chat message dto
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setSenderId(savedMessage.getSender().getId().toString());
        chatMessageDTO.setReceiverId(savedMessage.getReceiver().getId().toString());
        chatMessageDTO.setContent(savedMessage.getContent());
        chatMessageDTO.setTimestamp(savedMessage.getTimestamp());
        chatMessageDTO.setMessageId(savedMessage.getId().toString());

            MessageUpdateDTO messageUpdateDTO = new MessageUpdateDTO();
            messageUpdateDTO.setMessageId(savedMessage.getId().toString());
            messageUpdateDTO.setStatus(MessageStatusEnum.SENT);
            messageUpdateDTO.setMessageClientId(message.getMessageClientId());
            simpMessagingTemplate.convertAndSend(sendMessageUpdateTo, messageUpdateDTO);

        simpMessagingTemplate.convertAndSend(sendingTo, chatMessageDTO);
    }

    @MessageMapping("/chat/message.updates.delivered")
    public void updateMessageToDelivered(@RequestBody MessageUpdateDTO messageUpdate, Principal principal){
        _logger.info("Message DELIVERED update received: " + messageUpdate.toString());
        MessageEntity message = this.messageService.getMessageById(Long.parseLong(messageUpdate.getMessageId()));
        this.messageService.updateAllPreviousMessages(message);
        if(message == null){
            _logger.error("Message not found");
            return;
        }
        String sendingTo = "/topic/"+"message.updates.delivered/"+message.getSender().getId();
        MessageUpdateDTO messageUpdateDTO = new MessageUpdateDTO();
        messageUpdateDTO.setMessageId(message.getId().toString());
        messageUpdateDTO.setStatus(MessageStatusEnum.DELIVERED);
        messageUpdateDTO.setMessageClientId(message.getMessageClientId());
        simpMessagingTemplate.convertAndSend(sendingTo, messageUpdateDTO);
    }

    @MessageMapping("/chat/message.updates.read")
    public void updateMessageToRead(@RequestBody MessageUpdateDTO messageUpdate, Principal principal){
        _logger.info("Message update received: " + messageUpdate.toString());
        MessageEntity message = this.messageService.updateMessage(messageUpdate);
        if(message == null){
            _logger.error("Message not found");
            return;
        }
        String sendingTo = "/topic/"+"message.updates.read/"+message.getSender().getId();
        MessageUpdateDTO messageUpdateDTO = new MessageUpdateDTO();
        messageUpdateDTO.setMessageId(message.getId().toString());
        messageUpdateDTO.setStatus(MessageStatusEnum.READ);
        messageUpdateDTO.setMessageClientId(message.getMessageClientId());
        simpMessagingTemplate.convertAndSend(sendingTo, messageUpdateDTO);
    }


}
