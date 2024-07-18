package com.ranjit.todo.todo.dtos;

import com.ranjit.todo.todo.enums.MessageStatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageUpdateDTO {
    private String messageClientId;
    private String messageId;
    private MessageStatusEnum status;
}
