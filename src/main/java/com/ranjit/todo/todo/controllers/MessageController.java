package com.ranjit.todo.todo.controllers;

import com.ranjit.todo.todo.dtos.ChatMessageDTO;
import com.ranjit.todo.todo.dtos.ResponseBody;
import com.ranjit.todo.todo.entities.MessageEntity;
import com.ranjit.todo.todo.services.MessageService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/messages")
public class MessageController {
    Logger _logger = LoggerFactory.getLogger(MessageController.class);

    private final MessageService _messageService;
    private final ModelMapper _modelMapper;
    MessageController(MessageService messageService, ModelMapper modelMapper){
        this._messageService = messageService;
        this._modelMapper = modelMapper;
    };

    @GetMapping(path = "")
    public List<MessageEntity> getMessages(){
        return this._messageService.getAllMessages();
    }

    @GetMapping(path="/undelivered")
    public ResponseEntity<ResponseBody<List<ChatMessageDTO>>> getUndeliveredMessages(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<MessageEntity> undeliveredMessages =  this._messageService.getUndeliveredMessages(userDetails.getUsername());

        List<ChatMessageDTO> chatMessageDTOS = undeliveredMessages.stream()
                .map(messageEntity -> {
                    ChatMessageDTO chatMessageDTO = _modelMapper.map(messageEntity, ChatMessageDTO.class);
                    chatMessageDTO.setSenderId(messageEntity.getSender().getId().toString());
                    chatMessageDTO.setReceiverId(messageEntity.getReceiver().getId().toString());
                    chatMessageDTO.setTimestamp(messageEntity.getTimestamp());
                    chatMessageDTO.setMessageId(messageEntity.getId().toString());
                    chatMessageDTO.setContent(messageEntity.getContent());

                    return chatMessageDTO;
                })
                .toList();
        ResponseBody<List<ChatMessageDTO>> responseBody = ResponseBody.success(chatMessageDTOS);
        return ResponseEntity.ok(responseBody);
    }
}
