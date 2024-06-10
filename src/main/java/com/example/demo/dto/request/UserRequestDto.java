package com.example.demo.dto.request;

import lombok.*;


// set all the annotations for getter/setter and constructors with/without args.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// Dto : data transfer object - used to store data that we want to show
public class UserRequestDto {

    private Integer id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;

}

