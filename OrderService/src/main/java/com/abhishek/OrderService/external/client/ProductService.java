package com.abhishek.OrderService.external.client;

import com.abhishek.OrderService.exception.CustomException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
@CircuitBreaker(name = "external",fallbackMethod = "fallBack")
@FeignClient(name = "PRODUCT-SERVICE/product")
public interface ProductService {

    @PutMapping("/reduceQuantity/{id}")
    ResponseEntity<Void> reduceProductQuantity(@PathVariable("id") long productId, @RequestParam long quantity);
    default void fallBack(Exception e){
        System.out.println("============PRODUCT SERVICE IS NOT AVAILABLE=================");
        throw new CustomException("Product Service is not available","NOT_AVAILABLE", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
