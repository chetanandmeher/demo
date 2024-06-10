package com.example.demo.service.impl;

import com.example.demo.dto.request.UserRequestDto;
//import com.example.demo.mysql.model.User;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.mysql.model.User;
import com.example.demo.mysql.model.UserCredentials;
import com.example.demo.repository.UserCredentialsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.IUserService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.hash.Hashing;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCredentialsRepository userCredentialsRepository;

    @Override
    public UserResponseDto getUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        Optional<UserCredentials> userCredentials = userCredentialsRepository.findById(id);
        UserResponseDto userResponseDto = new UserResponseDto();
        if (user.isPresent()) {
            userResponseDto = convertUserToUserResponseDto(user.get(),userCredentials.get().getUsername());
        } else {
            System.out.println("Couldn't find user with id " + id);
        }
        return userResponseDto;
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        Iterable<UserCredentials> usersCredentials = userCredentialsRepository.findAll();
        List<UserResponseDto> userResponseDtoList = new ArrayList<UserResponseDto>();
        List<User> userList = StreamSupport.stream(users.spliterator(), false).toList();
        List<UserCredentials> usersCredentialsList = StreamSupport.stream(usersCredentials.spliterator(),false).toList();
        int numOfUsers = userList.size();
        for ( int i=0; i< numOfUsers; i++) {
           UserResponseDto tempUserResponseDto = convertUserToUserResponseDto(userList.get(i),
                                                               usersCredentialsList.get(i).getUsername());
            userResponseDtoList.add(tempUserResponseDto);
        }
        return userResponseDtoList;

    }


    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        // Below code saves data in users table
        // Convert the userRequestDto to User type
        User user = convertUserRequestDtoToUser(userRequestDto);
        // Set createdBy and createdAt
        user.setCreatedBy(1);
        user.setCreatedAt(LocalDateTime.now());
        // save the users
        userRepository.save(user);

        // Below code saves data in user_credentials table
        UserCredentials userCredentials = new UserCredentials();
        // General a random UUID string
        String uuid = UUID.randomUUID().toString();
        System.out.println("UUID :" + uuid);
        // Create the final password and hash it.
        final String computedPassword = Hashing.sha256()
                .hashString(userRequestDto.getPassword(), StandardCharsets.UTF_8).toString() + uuid;
        // Set all the parameters for the userCredentials
        /* The user id is not given in the userResponseDto
           So we have to get it from the save user Model in database.*/
        userCredentials.setUserId(user.getId());
        userCredentials.setUsername(userRequestDto.getUsername());
        userCredentials.setPassword(computedPassword);
        userCredentials.setPasswordSalt(uuid);
        userCredentials.setLoginDateTime(LocalDateTime.now());
        userCredentials.setCreatedBy(1);
        userCredentials.setCreatedAt(LocalDateTime.now());
        userCredentials.setUpdatedBy(1);
        userCredentials.setUpdatedAt(LocalDateTime.now());
        userCredentialsRepository.save(userCredentials);

        // create a userResponseDto
        UserResponseDto userResponseDto = new UserResponseDto();
        // set the userResponseDto parameters
        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(userCredentials.getUsername());
        userResponseDto.setFirstName(user.getFirstName());
        // return userResponseDto
        return userResponseDto;
    }

    @Override
    public UserRequestDto updateUser(UserRequestDto userDto) {
        Optional<User> userModel = userRepository.findById(userDto.getId());
        if (userModel.isPresent()) {
            userModel.get().setUsername(userDto.getUsername());
            userModel.get().setFirstName(userDto.getFirstName());
            userModel.get().setLastName(userDto.getLastName());
            userModel.get().setMobile(userDto.getMobile());
            userModel.get().setEmail(userDto.getEmail());
            userModel.get().setUpdatedBy(1);
            userModel.get().setUpdatedAt(LocalDateTime.now());
            userModel = Optional.of(userRepository.save(userModel.get()));
            return convertUserToUserRequestDto(userModel.get(), );
        } else {
            System.out.println("User data with id: " + userDto.getId() + " not found");
        }
        return userDto;
    }

    @Override
    public UserRequestDto updatePartialUserById(UserRequestDto userDto, Integer id) {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            // check and update username, firstName, lastName, mobile, email, updatedBy, updatedAt
            user.get().setUsername(userDto.getUsername() != null && !userDto.getUsername().equals(user.get().getUsername()) ? userDto.getUsername() : user.get().getUsername());
            user.get().setFirstName(userDto.getFirstName() != null && !userDto.getFirstName().equals(user.get().getFirstName()) ? userDto.getFirstName() : user.get().getFirstName());
            user.get().setLastName(userDto.getLastName() != null && !userDto.getLastName().equals(user.get().getLastName()) ? userDto.getLastName() : user.get().getLastName());
            user.get().setMobile(userDto.getMobile() != null && !userDto.getMobile().equals(user.get().getMobile()) ? userDto.getMobile() : user.get().getMobile());
            user.get().setEmail(userDto.getEmail() != null && !userDto.getEmail().equals(user.get().getEmail()) ? userDto.getEmail() : user.get().getEmail());
            user.get().setUpdatedBy(2);
            user.get().setUpdatedAt(LocalDateTime.now());
            userRepository.save(user.get());
            return convertUserToUserRequestDto(user.get());
        } else {
            System.out.println("User with id: " + userDto.getId() + "not found.");
        }
        return userDto;
    }

    @Override
    public void deleteUserById(Integer id) {
        // retrieve the given user from db
        Optional<User> user = userRepository.findById(id);
        // delete the user from data base
        userRepository.deleteById(id);

    }


    private User convertUserRequestDtoToUser(UserRequestDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setMobile(userDto.getMobile());
        user.setEmail(userDto.getEmail());
        user.setCreatedBy(1);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedBy(1);
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    private UserRequestDto convertUserToUserRequestDto(User user) {
        UserRequestDto userDto = new UserRequestDto();
        userDto.setId(user.getId());
//        userDto.setUsername(userCredentials.getUsername());
//        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setMobile(user.getMobile());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private UserResponseDto convertUserToUserResponseDto(User user, String userCredentialsUsername) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(userCredentialsUsername);
        userResponseDto.setFirstName(user.getFirstName());
        return userResponseDto;


    }
}
