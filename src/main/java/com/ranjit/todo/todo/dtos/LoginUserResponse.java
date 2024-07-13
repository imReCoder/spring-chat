package com.ranjit.todo.todo.dtos;

import lombok.Data;

@Data
public class LoginUserResponse {
    String token;
    Long expiresIn;
}
