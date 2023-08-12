package com.abhishek.ProductService.exception;

import com.abhishek.ProductService.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductExceptionHandler(
            ProductNotFoundException productNotFoundException) {
        ErrorResponse errorResponse = ErrorResponse.builder().errorMessage(productNotFoundException.getMessage())
                .errorCode(productNotFoundException.getErrorCode()).build();
        return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
    }

}
