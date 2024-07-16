package com.ranjit.todo.todo.controllers;

import com.ranjit.todo.todo.dtos.LoginUserDTO;
import com.ranjit.todo.todo.dtos.LoginUserResponse;
import com.ranjit.todo.todo.dtos.RegisterUserDTO;
import com.ranjit.todo.todo.dtos.UserDTO;
import com.ranjit.todo.todo.entities.UserEntity;
import com.ranjit.todo.todo.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    final AuthService _authService;

    public AuthController(AuthService _authService) {
        this._authService = _authService;
    }

//    @GetMapping(path = "")
//    public UserDTO getUser() {
//        return new UserDTO(new Long(123), "Ranjit Kumar Pandit", "ranjitkumar448@yahoo.com", "Test@123", "https://www.google.com", "google");
//    }

    ;

    @PostMapping(path = "/register")
    public ResponseEntity<UserEntity> registerUser(@RequestBody RegisterUserDTO userBody) {
        UserEntity registeredUser = this._authService.registerUser(userBody);
        return ResponseEntity.ok(registeredUser);
    }


    @PostMapping(path = "/login")
    public ResponseEntity<LoginUserResponse> loginUser(@RequestBody LoginUserDTO loginDto) {
        UserEntity loggedInUser = this._authService.loginUser(loginDto);
        String token = this._authService.generateToken(loggedInUser);
        LoginUserResponse response = new LoginUserResponse();
        response.setToken(token);
        response.setExpiresIn(3600L);
        return ResponseEntity.ok(response);
    }


}
