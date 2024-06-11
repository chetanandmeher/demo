package com.example.demo.service;

import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.UserResponseDto;

import java.util.List;

public interface IUserService {

    public UserResponseDto getUserById(Integer id);

    public List<UserResponseDto> getAllUsers();

    public UserResponseDto createUser(UserRequestDto userDto);

    public UserResponseDto updateUser(UserRequestDto userDto);

    public UserResponseDto updatePartialUserById(UserRequestDto userDto, Integer id);

    public void deleteUserById(Integer Id);

}
