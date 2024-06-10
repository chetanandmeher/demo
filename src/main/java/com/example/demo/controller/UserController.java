package com.example.demo.controller;

import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {


    @Autowired
    IUserService userService;

    @RequestMapping(value = "/v1/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUser(@PathVariable("id") Integer id) {
        UserResponseDto userResponseDto = userService.getUserById(id);
        System.out.println("Service Constructor: " + userService.hashCode());
        return ResponseEntity.ok(userResponseDto);
    }

    @RequestMapping(value = "/v1/users", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllUsers() {
        List<UserResponseDto> userResponseDtoList = userService.getAllUsers();
        return (ResponseEntity.ok(userResponseDtoList));
    }

    @RequestMapping(value = "/v1/users", method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(@RequestBody UserRequestDto userDto) {
        UserResponseDto userResponseDto = userService.createUser(userDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @RequestMapping(value = "/v1/users",method = RequestMethod.PUT)
    public ResponseEntity<Object> updateUser(@RequestBody UserRequestDto userDto) {
        UserRequestDto userDtoReturn = userService.updateUser(userDto);
        return ResponseEntity.ok(userDtoReturn);
    }

    @RequestMapping(value = "/v1/users/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Object> updatePartialUserById(@RequestBody UserRequestDto userDto, @PathVariable("id") Integer id) {
        UserRequestDto userDtoReturn = userService.updatePartialUserById(userDto, id);
        return  ResponseEntity.ok(userDtoReturn);
    }

    @RequestMapping(value = "/v1/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUserVyId(@PathVariable("id") Integer id) {
        UserRequestDto userDto = userService.getUserById(id);
        String deletedUser = userDto.toString();
        userService.deleteUserById(id);
        return ResponseEntity.ok("This user is deleted from database: " + deletedUser);
    }


}
