package com.abhishek.ProductService.exception;

import com.abhishek.ProductService.entity.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleProductExceptionHandler(ProductNotFoundException productNotFoundException){
        ErrorMessage message = ErrorMessage.builder()
                .message(productNotFoundException.getMessage())
                .errorCode(productNotFoundException.getErrorCode())
                .build();
        return new ResponseEntity(message, HttpStatus.NOT_FOUND);
    }

}
