package com.example.demo.service.impl;

import com.example.demo.dto.UserDto;
//import com.example.demo.mysql.model.User;
import com.example.demo.mysql.model.UserModel;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDto getUserById(Integer id) {
        Optional<UserModel> userModel = userRepository.findById(id);
        UserDto userDto = new UserDto();
        if (userModel.isPresent()) {
            userDto = convertUserModelToUserDto(userModel.get());
        }
        else {
            System.out.println("Couldn't find user with id " + id);
        }
        return userDto;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        UserModel userModel = convertUserDtoToUserModel(userDto);
        userModel = userRepository.save(userModel);
        return convertUserModelToUserDto(userModel);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        Optional<UserModel> userModel = userRepository.findById(userDto.getId());
        if (userModel.isPresent()) {
            userModel.get().setUsername(userDto.getUsername());
            userModel.get().setFirstName(userDto.getFirstName());
            userModel.get().setLastName(userDto.getLastName());
            userModel.get().setMobile(userDto.getMobile());
            userModel.get().setEmail(userDto.getEmail());
            userModel.get().setUpdatedBy(1);
            userModel.get().setUpdatedAt(LocalDateTime.now());
            userModel = Optional.of(userRepository.save(userModel.get()));
            return convertUserModelToUserDto(userModel.get());
        }
        else {System.out.println("User data with id: " + userDto.getId() + " not found");}
        return userDto;
    }

    @Override
    public UserDto updatePartialUser(UserDto userDto) {
        Optional<UserModel> userModel = userRepository.findById(userDto.getId());
        if (userModel.isPresent()) {
            // check and update username, firstName, lastName, mobile, email, updatedBy, updatedAt
            userModel.get().setUsername(userDto.getUsername() != null && !userModel.get().getUsername().equals(userDto.getUsername()) ? userDto.getUsername() : userModel.get().getUsername());
            userModel.get().setFirstName(userDto.getFirstName() != null && !userModel.get().getFirstName().equals(userDto.getFirstName()) ? userDto.getFirstName() : userModel.get().getFirstName());
            userModel.get().setLastName(userDto.getLastName() != null && !userModel.get().getLastName().equals(userDto.getLastName()) ? userDto.getLastName() : userModel.get().getLastName());
            userModel.get().setMobile(userDto.getMobile() != null && !userModel.get().getMobile().equals(userDto.getMobile()) ? userDto.getMobile() : userModel.get().getMobile());
            userModel.get().setEmail(userDto.getEmail() != null && !userModel.get().getEmail().equals(userDto.getEmail()) ? userDto.getEmail() : userModel.get().getEmail());
            userModel.get().setUpdatedBy(2);
            userModel.get().setUpdatedAt(LocalDateTime.now());
            userRepository.save(userModel.get());
            return convertUserModelToUserDto(userModel.get());
        }
        else{
            System.out.println("User with id: " + userDto.getId()+ "not found.");
        }
        return userDto;
    }

    @Override
    public void deleteUserById(Integer id) {
        // retrieve the given user from db
        Optional<UserModel> userModel = userRepository.findById(id);
        // delete the user from data base
        userRepository.deleteById(id);

    }


    private UserModel convertUserDtoToUserModel(UserDto userDto) {
        UserModel userModel = new UserModel();
        userModel.setUsername(userDto.getUsername());
        userModel.setFirstName(userDto.getFirstName());
        userModel.setLastName(userDto.getLastName());
        userModel.setMobile(userDto.getMobile());
        userModel.setEmail(userDto.getEmail());
        userModel.setCreatedBy(1);
        userModel.setCreatedAt(LocalDateTime.now());
        userModel.setUpdatedBy(1);
        userModel.setUpdatedAt(LocalDateTime.now());
        return userModel;
    }

    private UserDto convertUserModelToUserDto(UserModel userModel) {
        UserDto userDto = new UserDto();
        userDto.setId(userModel.getId());
        userDto.setUsername(userModel.getUsername());
        userDto.setFirstName(userModel.getFirstName());
        userDto.setLastName(userModel.getLastName());
        userDto.setMobile(userModel.getMobile());
        userDto.setEmail(userModel.getEmail());
        return userDto;
    }
}
