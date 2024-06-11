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
        Optional<UserCredentials> userCredentials = userCredentialsRepository.findByUserId(id);
        UserResponseDto userResponseDto = new UserResponseDto();
        if (user.isPresent()) {
            userResponseDto = getUserResponseDto(user.get(), userCredentials.get());
        } else {
            System.out.println("Couldn't find user with id " + id);
        }
        return userResponseDto;
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        Iterable<UserCredentials> usersCredentials = userCredentialsRepository.findAll();
        // convert Iterables users to List users
//        List<User> userList = StreamSupport.stream(users.spliterator(), false).toList();
        // convert Iterables usersCredentials to List usersCredentials
//        List<UserCredentials> usersCredentialsList = StreamSupport.stream(usersCredentials.spliterator(),false).toList();

        // Convert the userResponseDto
        List<UserResponseDto> userResponseDtoList = new ArrayList<UserResponseDto>();
        for (User user : users) {
            Optional<UserCredentials> userCredentials = userCredentialsRepository.findByUserId(user.getId());
            UserResponseDto tempUserResponseDto = getUserResponseDto(user, userCredentials.get());
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
        // Set createdBy, createdAt, updatedBy, updatedAt
        user.setCreatedBy(1);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedBy(1);
        user.setUpdatedAt(LocalDateTime.now());
        // save the users
        userRepository.save(user);

        // Below code saves data in user_credentials table
        UserCredentials userCredentials = new UserCredentials();
        // Generate a random UUID string
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

        // return userResponseDto from db
        return getUserResponseDto(user,userCredentials);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UserRequestDto userRequestDto) {
        Optional<User> userModel = userRepository.findById(userRequestDto.getId());
        Optional<UserCredentials> userCredentials = userCredentialsRepository.findById(userRequestDto.getId());
        UserResponseDto userResponseDto = new UserResponseDto();
        // update in user table
        if (userModel.isPresent()) {
            // this line of code save username in userCredentials table
            userCredentials.get().setUsername(userRequestDto.getUsername());
            userCredentials.get().setUpdatedBy(2);
            userCredentials.get().setUpdatedAt(LocalDateTime.now());
            userCredentialsRepository.save(userCredentials.get());

            // for users table
            userModel.get().setFirstName(userRequestDto.getFirstName());
            userModel.get().setLastName(userRequestDto.getLastName());
            userModel.get().setMobile(userRequestDto.getMobile());
            userModel.get().setEmail(userRequestDto.getEmail());
            userModel.get().setUpdatedBy(1);
            userModel.get().setUpdatedAt(LocalDateTime.now());
            userRepository.save(userModel.get());
            // get the data from db and set to userResponseDto
            Optional<User> userReturn = userRepository.findById(userRequestDto.getId());
            Optional<UserCredentials> userCredentialsReturn = userCredentialsRepository.findByUserId(userRequestDto.getId());
            return getUserResponseDto(userReturn.get(), userCredentialsReturn.get());
        } else {
            System.out.println("User data with id: " + userRequestDto.getId() + " not found");
        }
        return userResponseDto;
    }

    @Override
    @Transactional
    public UserResponseDto updatePartialUserById(UserRequestDto userRequestDto, Integer id) {
        Optional<User> user = userRepository.findById(id);
        Optional<UserCredentials> userCredentials = userCredentialsRepository.findByUserId(id);
        UserResponseDto userResponseDto = new UserResponseDto();
        if (user.isPresent()) {
            // check and update username, firstName, lastName, mobile, email, updatedBy, updatedAt
            // from users table
            user.get().setFirstName(userRequestDto.getFirstName() != null && !userRequestDto.getFirstName().equals(user.get().getFirstName()) ? userRequestDto.getFirstName() : user.get().getFirstName());
            user.get().setLastName(userRequestDto.getLastName() != null && !userRequestDto.getLastName().equals(user.get().getLastName()) ? userRequestDto.getLastName() : user.get().getLastName());
            user.get().setMobile(userRequestDto.getMobile() != null && !userRequestDto.getMobile().equals(user.get().getMobile()) ? userRequestDto.getMobile() : user.get().getMobile());
            user.get().setEmail(userRequestDto.getEmail() != null && !userRequestDto.getEmail().equals(user.get().getEmail()) ? userRequestDto.getEmail() : user.get().getEmail());
            user.get().setUpdatedBy(2);
            user.get().setUpdatedAt(LocalDateTime.now());
            userRepository.save(user.get());

            // from user Credentials table
            userCredentials.get().setUsername(userRequestDto.getUsername() != null && !userRequestDto.getUsername().equals(userCredentials.get().getUsername()) ? userRequestDto.getUsername() : userCredentials.get().getUsername());
            userCredentials.get().setUpdatedBy(2);
            userCredentials.get().setUpdatedAt(LocalDateTime.now());
            userCredentialsRepository.save(userCredentials.get());

            // get the data from db and set to userResponseDto
            Optional<User> userReturn = userRepository.findById(userRequestDto.getId());
            Optional<UserCredentials> userCredentialsReturn = userCredentialsRepository.findByUserId(userRequestDto.getId());
            return getUserResponseDto(userReturn.get(), userCredentialsReturn.get());
        } else {
            System.out.println("User with id: " + userRequestDto.getId() + "not found.");
        }
        return userResponseDto;
    }

    @Override
    @Transactional
    public void deleteUserById(Integer id) {
        // retrieve the given user from db
        Optional<User> user = userRepository.findById(id);
        Optional<UserCredentials> userCredentials = userCredentialsRepository.findByUserId(id);
        // delete the user from data base
        userRepository.deleteById(id);
        userCredentialsRepository.deleteByUserId(id);
    }

    private User convertUserRequestDtoToUser(UserRequestDto userDto) {
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .mobile(userDto.getMobile())
                .email(userDto.getEmail())
                .createdBy(1)
                .createdAt(LocalDateTime.now())
                .updatedBy(1)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private UserRequestDto convertUserToUserRequestDto(User user) {

        return UserRequestDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .mobile(user.getMobile())
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }

    private UserResponseDto convertUserToUserResponseDto(User user, String userCredentialsUsername) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(userCredentialsUsername)
                .firstName(user.getFirstName())
                .build();
    }

    private UserResponseDto getUserResponseDto(User user, UserCredentials userCredentials) {
        // Create userResponseDto and set the parameters.
        return UserResponseDto.builder()
                .id(user.getId())
                .username(userCredentials.getUsername())
                .firstName(user.getFirstName())
                .build();
    }


}
