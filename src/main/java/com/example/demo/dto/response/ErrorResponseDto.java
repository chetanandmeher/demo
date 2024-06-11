package com.example.demo.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDto {

    private int httpStatusCode;
    private String errorMessage;
}
