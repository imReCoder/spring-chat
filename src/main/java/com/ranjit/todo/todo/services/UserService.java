package com.ranjit.todo.todo.services;

import com.ranjit.todo.todo.dtos.UserDTO;
import com.ranjit.todo.todo.entities.UserEntity;
import com.ranjit.todo.todo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final Logger _logger = org.slf4j.LoggerFactory.getLogger(UserService.class);

    private final ModelMapper _modelMapper;
    private final UserRepository _userRepository;

    public UserService(ModelMapper _modelMapper, UserRepository _userRepository) {
        this._modelMapper = _modelMapper;
        this._userRepository = _userRepository;
    }

    public UserDTO getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return _modelMapper.map(userDetails, UserDTO.class);
    }

    public UserDTO getUserById(Long id) {
        Optional<UserEntity> user = _userRepository.findById(id);
        return user.map(userEntity -> _modelMapper.map(userEntity, UserDTO.class)).orElse(null);
    }

    public List<UserDTO> searchUsers(String searchKey) {
        _logger.info("Searching for user with key: {}", searchKey);
        try {
            Pageable pageable = PageRequest.of(0, 10);
            List<UserEntity> users = _userRepository.searchByNameOrEmail(searchKey,pageable);
            _logger.info("User found: {}", users);
            List<UserDTO> userDTOs = users.stream().map(userEntity -> _modelMapper.map(userEntity, UserDTO.class)).toList();

            _logger.info("UserDTO: {}", userDTOs);
            return userDTOs;
        } catch (Exception e) {
            return List.of();
        }
    }
}
