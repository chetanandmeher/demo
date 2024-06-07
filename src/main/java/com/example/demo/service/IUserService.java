package com.example.demo.service;

import com.example.demo.dto.UserDto;

public interface IUserService {

    public UserDto getUserById(Integer id);

    public UserDto createUser(UserDto userDto);

    public UserDto updateUser(UserDto userDto);

    public UserDto updatePartialUser(UserDto userDto);

    public void deleteUserById(Integer Id);

}
