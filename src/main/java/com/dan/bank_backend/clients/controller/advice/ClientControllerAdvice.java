package com.dan.bank_backend.clients.controller.advice;

import com.dan.bank_backend.clients.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClientControllerAdvice {

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<String> handleClientNotFound(ClientNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(ClientNotFoundByIdentificationNumberException.class)
    public ResponseEntity<String> handleClientNotFoundByIdentificationNumber(ClientNotFoundByIdentificationNumberException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(ClientEmailAlreadyExistsException.class)
    public ResponseEntity<String> handleClientEmailAlreadyExists(ClientEmailAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    @ExceptionHandler(ClientIdentificationNumberAlreadyExists.class)
    public ResponseEntity<String> handleClientIdentificationNumberAlreadyExists(ClientIdentificationNumberAlreadyExists ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    @ExceptionHandler(UnderAgeRestrictionException.class)
    public ResponseEntity<String> handleUnderAgeRestriction(UnderAgeRestrictionException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(InvalidBirthdayException.class)
    public ResponseEntity<String> handleInvalidBirthday(InvalidBirthdayException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
