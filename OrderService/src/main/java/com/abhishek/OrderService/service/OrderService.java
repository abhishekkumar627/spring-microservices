package com.abhishek.OrderService.service;

import com.abhishek.OrderService.model.OrderRequest;

public interface OrderService {
    Long placeOrder(OrderRequest orderRequest);
}
