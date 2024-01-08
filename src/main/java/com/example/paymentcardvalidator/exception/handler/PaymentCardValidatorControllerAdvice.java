package com.example.paymentcardvalidator.exception.handler;

import com.example.paymentcardvalidator.dto.ExceptionResponse;
import com.example.paymentcardvalidator.exception.InvalidCardDetailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PaymentCardValidatorControllerAdvice {

    @ExceptionHandler(value = {InvalidCardDetailException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleInvalidCardDetailExceptions(InvalidCardDetailException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(e.getMessage()));
    }

}
