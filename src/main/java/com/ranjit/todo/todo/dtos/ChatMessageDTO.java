package com.ranjit.todo.todo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private String senderId;
    private String receiverId;
    private String content;
}
