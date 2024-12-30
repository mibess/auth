package com.mibess.loginserver.exception;

import lombok.Getter;

@Getter
public enum ProblemType {
    GENERIC_ERROR("Something wrong. Try again later."),
    BUSINESS_ERROR("An error occurred while processing your request. Please try again later."),
    VALIDATION_ERROR("One or more fields are invalid. Fill in correctly and try again."),;

    private final String message;

    ProblemType(String message) {
        this.message = message;
    }

}
