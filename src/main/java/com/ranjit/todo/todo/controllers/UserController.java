package com.ranjit.todo.todo.controllers;

import com.ranjit.todo.todo.dtos.ResponseBody;
import com.ranjit.todo.todo.dtos.UserDTO;
import com.ranjit.todo.todo.dtos.UserStatusUpdate;
import com.ranjit.todo.todo.dtos.UserStatusUpdatesBodyDTO;
import com.ranjit.todo.todo.services.UserService;
import jakarta.websocket.server.PathParam;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "users")
public class UserController {

    private final UserService userService;
    private final Logger _logger = LoggerFactory.getLogger(UserController.class);
    private ModelMapper _modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this._modelMapper = modelMapper;
    }

    @GetMapping("/get")
    ResponseEntity<ResponseBody<UserDTO>> getUser() {
        UserDTO user = userService.getCurrentUser();
        ResponseBody<UserDTO> res = ResponseBody.success(user);
        return ResponseEntity.ok(res);
    }

    ;

    @GetMapping("/get/{id}")
    ResponseEntity<ResponseBody<UserDTO>> getUser(@PathVariable("id") Long id) {
        System.out.println("User ID: " + id);
        UserDTO user = userService.getUserById(id);
        ResponseBody<UserDTO> res = ResponseBody.success(user);
        return ResponseEntity.ok(res);
    }

    ;

    @GetMapping("/search")
    ResponseEntity<ResponseBody<List<UserDTO>>> searchUser(@RequestParam("searchKey") String searchKey) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserDTO> users = userService.searchUsers(searchKey);
        ResponseBody<List<UserDTO>> res = ResponseBody.success(users);
        return ResponseEntity.ok(res);
    }

    ;

    @PostMapping("/status")
    public ResponseEntity<ResponseBody<List<UserStatusUpdate>>> getUsersStatus(@RequestBody UserStatusUpdatesBodyDTO body) {
        List<UserStatusUpdate> status =  userService.getUsersStatus(body.getUserIds());
        ResponseBody<List<UserStatusUpdate>> res = ResponseBody.success(status);
        return ResponseEntity.ok(res);
    }
}
