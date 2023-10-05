package com.abhishek.OrderService.controller;

import com.abhishek.OrderService.model.OrderRequest;
import com.abhishek.OrderService.model.OrderResponse;
import com.abhishek.OrderService.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAuthority('Customer')")
    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest) {
        Long orderId = orderService.placeOrder(orderRequest);
        log.info("Order Id : {}", orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer')")
    @GetMapping("/getOrderDetails/{id}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable(name = "id") Long orderId) {
        OrderResponse orderResponse = orderService.getOrderDetails(orderId);
        log.info("Order Response : {}", orderResponse);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);

    }
}
