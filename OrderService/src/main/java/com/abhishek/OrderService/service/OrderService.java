package com.abhishek.OrderService.service;

import com.abhishek.OrderService.model.OrderRequest;
import com.abhishek.OrderService.model.OrderResponse;

public interface OrderService {
    Long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(Long orderId);
}
