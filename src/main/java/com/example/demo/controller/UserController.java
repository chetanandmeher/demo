package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.mysql.model.UserModel;
import com.example.demo.service.IUserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {


    @Autowired
    IUserService userService;

    @RequestMapping(value = "/v1/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUser(@PathVariable("id") Integer id) {
        UserDto userDto = userService.getUserById(id);
        System.out.println("Service Constructor: " + userService.hashCode());
        return ResponseEntity.ok(userDto);
    }

    @RequestMapping(value = "/v1/users", method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(@RequestBody UserDto userDto) {
        UserDto userDtoReturn = userService.createUser(userDto);
        return ResponseEntity.ok(userDtoReturn);
    }

    @RequestMapping(value = "/v1/users",method = RequestMethod.PUT)
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto) {
        UserDto userDtoReturn = userService.updateUser(userDto);
        return ResponseEntity.ok(userDtoReturn);
    }

    @RequestMapping(value = "/v1/users", method = RequestMethod.PATCH)
    public ResponseEntity<Object> updatePartialUser(@RequestBody UserDto userDto) {
        UserDto userDtoReturn = userService.updatePartialUser(userDto);
        return ResponseEntity.ok(userDtoReturn);
    }

    @RequestMapping(value = "/v1/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUserVyId(@PathVariable("id") Integer id) {
        UserDto userDto = userService.getUserById(id);
        String deletedUser = userDto.toString();
        userService.deleteUserById(id);
        return ResponseEntity.ok("This user is deleted from database: " + deletedUser);
    }


}
