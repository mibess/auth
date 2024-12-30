package com.mibess.loginserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetails {

    private final int status;
    private final String message;
    private final List<String> errors;
    private final String path;
    private final LocalDateTime timestamp = LocalDateTime.now();

}
