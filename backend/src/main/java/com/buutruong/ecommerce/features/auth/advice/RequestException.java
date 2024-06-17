package com.buutruong.ecommerce.features.auth.advice;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RequestException {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String>  handleInvalidRequest(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> errors.put(error.getField(), error.getDefaultMessage()));

        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UsernameNotFoundException.class)
    public Map<String, String>  handleEmailNotFound(UsernameNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());

        return errors;
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public Map<String, String> handleExpiredJwtException(ExpiredJwtException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "JWT token expired. Please log in again.");
        errors.put("details", ex.getMessage());
        return errors;
    }
}
