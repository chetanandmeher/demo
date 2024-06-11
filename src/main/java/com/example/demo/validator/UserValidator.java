package com.example.demo.validator;

import jakarta.validation.constraints.Past;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class UserValidator {

    public boolean validateAge(@Past(message = "Enter a valid date.") LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        } else {
            LocalDate currentDate = LocalDate.now();
            System.out.println("Today Date: " + currentDate);
            long years = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
            System.out.println("Age: " + years);
            return years >= 18;
        }
    }
}