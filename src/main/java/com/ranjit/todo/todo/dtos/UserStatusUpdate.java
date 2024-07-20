package com.ranjit.todo.todo.dtos;

import lombok.Data;

@Data
public class UserStatusUpdate {
    private String userId;
    private boolean status;
}
