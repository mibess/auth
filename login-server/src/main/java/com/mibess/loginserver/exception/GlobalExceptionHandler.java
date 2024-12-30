package com.mibess.loginserver.exception;

import com.mibess.loginserver.exception.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Internal Server Error: {}", ex.getMessage(), ex);
        return buildErrorResponse(ProblemType.GENERIC_ERROR.getMessage(), List.of(ex.getMessage()), request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        log.error("Business exception {}", ex.getMessage());
        return buildErrorResponse(ProblemType.BUSINESS_ERROR.getMessage(), List.of(ex.getMessage()),  request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request) {
        log.error("Validation exception {}", ex.getMessage());
        return buildErrorResponse(ProblemType.VALIDATION_ERROR.getMessage(), List.of(ex.getMessage()), request, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {

        List<String> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                //.map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .map(error -> String.format("%s", error.getDefaultMessage()))
                .toList();

        return buildErrorResponse(ProblemType.VALIDATION_ERROR.getMessage(), errorMessages, request, HttpStatus.BAD_REQUEST);
    }


    private ResponseEntity<Object> buildErrorResponse(String message, List<String> errors, WebRequest request, HttpStatus status) {
        ErrorDetails errorDetails = new ErrorDetails(
                status.value(),
                message,
                errors,
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, status);
    }

}
