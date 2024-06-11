package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;


// set all the annotations for getter/setter and constructors with/without args.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)   // when we send to website it will show only those parameters whose value is not null

// Dto : data transfer object - used to store data that we want to show
public class UserRequestDto {

//    @Pattern(regexp = "[0-9]+", message = "'id' can only be positive integer")
    private Integer id;

    @NotNull(message = "'username' cannot be null!!")
    @Length(min = 5, max = 20, message = "'username' can have min 5 characters and max 20 characters")
    private String username;

    @NotNull(message = "'password' cannot be null!!")
    private String password;

    @NotNull(message = "'firstName' cannot be null!!")
    @Pattern(regexp = "[a-zA-Z]+", message = "'firstName' can only contain alphabets")
    @Length(max = 20, message = "'firstName' can only have 20 characters")
    private String firstName;

    @Pattern(regexp = "[a-zA-Z]+", message = "'lastName' can only contain alphabets")
    @Length(max = 20, message = "'lastName' can only have 20 characters")
    private String lastName;

    @Past(message = "Enter a valid date.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "[0-9]+", message = "'mobile' can only contain numbers")
    @Digits(fraction = 0, integer = 10, message = "'mobile' should have 10 digit")
    private String mobile;

    @Email(regexp = "/^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$/gm", message = "'email' is not valid!!")
    private String email;

}

