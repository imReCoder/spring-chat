package com.ranjit.todo.todo.services;

import com.ranjit.todo.todo.dtos.LoginUserDTO;
import com.ranjit.todo.todo.dtos.LoginUserResponse;
import com.ranjit.todo.todo.dtos.RegisterUserDTO;
import com.ranjit.todo.todo.dtos.UserDTO;
import com.ranjit.todo.todo.entities.UserEntity;
import com.ranjit.todo.todo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    final UserRepository _userRepository;
    final ModelMapper _modelMapper;
    final PasswordEncoder _passwordEncoder;
//    final AuthenticationManager _authenticationManager;
    final JwtService _jwtService;

    public AuthService(UserRepository _userRepository,
                       ModelMapper _modelMapper,
                       PasswordEncoder _passwordEncoder,
//                       AuthenticationManager _authenticationManager,
                       JwtService _jwtService) {
        this._userRepository = _userRepository;
        this._modelMapper = _modelMapper;
        this._passwordEncoder = _passwordEncoder;
//        this._authenticationManager = _authenticationManager;
        this._jwtService = _jwtService;
    }

    public UserEntity registerUser(RegisterUserDTO userDto) {

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDto.getEmail());
        userEntity.setName(userDto.getName());
        userEntity.setProfileImage(userDto.getProfileImage());
        userEntity.setProvider(userDto.getProvider());

        String encodedPassword = _passwordEncoder.encode(userDto.getPassword());
        userEntity.setPassword(encodedPassword);

        System.out.println(userDto.getPassword() + "-" +encodedPassword);

        System.out.println(userEntity);
        return _userRepository.save(userEntity);
    };

    public UserEntity loginUser(LoginUserDTO loginDto) {
        Optional<UserEntity> userEntity = _userRepository.findByEmail(loginDto.getEmail());
        System.out.println("Db user - " + userEntity);
//        _authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginDto.getEmail(),
//                        loginDto.getPassword()
//                )
//        );
        return userEntity.orElseThrow();
    }

    public String generateToken(UserEntity userEntity) {
        return _jwtService.generateToken(userEntity);
    }

    public ResponseEntity<LoginUserResponse> oauthLogin(RegisterUserDTO user){
        Optional<UserEntity> userExist = this._userRepository.findByEmail(user.getEmail());
        if(userExist.isEmpty()){
           this.registerUser(user);
        }
        LoginUserDTO loginDto = new LoginUserDTO();
        loginDto.setEmail(user.getEmail());
        loginDto.setPassword("password");
        UserEntity loggedInUser = this.loginUser(loginDto);
        String token = this.generateToken(loggedInUser);
        LoginUserResponse response = new LoginUserResponse();
        response.setToken(token);
        response.setExpiresIn(3600L);
        return ResponseEntity.ok(response);

    }
}
