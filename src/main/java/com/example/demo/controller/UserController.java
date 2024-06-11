package com.example.demo.controller;

import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.ErrorResponseDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.service.IUserService;
import com.example.demo.validator.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.net.URI;
import java.util.List;

@RestController
public class UserController {


    @Autowired
    IUserService userService;

    @Autowired
    UserValidator userValidator;

    @GetMapping("/v1/users/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") Integer id) {
        UserResponseDto userResponseDto = userService.getUserById(id);
        System.out.println("Service Constructor: " + userService.hashCode());
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/v1/users")
    public ResponseEntity<Object> getAllUsers() {
        List<UserResponseDto> userResponseDtoList = userService.getAllUsers();
        return (ResponseEntity.ok(userResponseDtoList));
    }


    @PostMapping("/v1/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        boolean isValidAge = userValidator.validateAge(userRequestDto.getDateOfBirth());
        if (!isValidAge) {
            ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                    .errorMessage("Enter a valid age.(greater than 18)")
                    .httpStatusCode(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(errorResponseDto);
        }
        UserResponseDto userResponseDto = userService.createUser(userRequestDto);
        return ResponseEntity.created(URI.create("/v1/users/" + userResponseDto.getId())).body(userResponseDto);
    }

    @PutMapping("/v1/users")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDtoReturn = userService.updateUser(userRequestDto);
        return ResponseEntity.ok(userResponseDtoReturn);
    }

    @PatchMapping("/v1/users/{id}")
    public ResponseEntity<Object> updatePartialUserById(@RequestBody UserRequestDto userDto, @PathVariable("id") Integer id) {
        UserRequestDto userDtoReturn = userService.updatePartialUserById(userDto, id);
        return  ResponseEntity.ok(userDtoReturn);
    }

    @DeleteMapping("/v1/users/{id}")
    public ResponseEntity<Object> deleteUserVyId(@PathVariable("id") Integer id) {
        UserResponseDto userResponseDto = userService.getUserById(id);
        String deletedUser = userResponseDto.toString();
        userService.deleteUserById(id);
        return ResponseEntity.ok("This user is deleted from database: " + deletedUser);
    }
}
