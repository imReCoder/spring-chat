package com.ranjit.todo.todo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {
    String name;
    String email;
    String password;
    String profileImage;
    String provider;
}
