package com.example.microbankingproject.handling;

import com.example.microbankingproject.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandling {
    @ExceptionHandler(ApplicationException.class)
    public ErrorResponse applicationException(ApplicationException exception) {
        CommonErrors commonErrors = exception.getCommonErrors();
        return ErrorResponse.
                builder().
                key(commonErrors.getKey()).
                status(commonErrors.getStatus()).
                message(commonErrors.getMessage()).
                timestamp(commonErrors.getTimestamp()).
                build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException exception) {
        log.warn("400 - Bad Request !");
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return errors;
    }
}
