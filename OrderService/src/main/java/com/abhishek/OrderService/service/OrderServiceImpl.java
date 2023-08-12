package com.abhishek.OrderService.service;

import com.abhishek.OrderService.entity.OrderEntity;
import com.abhishek.OrderService.external.client.ProductService;
import com.abhishek.OrderService.model.OrderRequest;
import com.abhishek.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Override
    public Long placeOrder(OrderRequest orderRequest) {
        // Order Entity - Save the data with status order created
        // Product Service - Block our products(Reduce the quantity)
        // Payments Service - Payments - Success- COMPLETE OTHERWISE CANCELLED.
        log.info("orderRequest Object : {}", orderRequest);

        log.info("Reducing quantity first before placing order ");

        productService.reduceProductQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("placing order with quantity requsted : {} ", orderRequest.getQuantity());
        OrderEntity entity = OrderEntity.builder().productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity()).orderDate(Instant.now()).orderStatus("CREATED")
                .amount(orderRequest.getTotalAmount()).build();
        entity = orderRepository.save(entity);
        log.info("Order placed successfully with order id : {}", entity.getId());
        return entity.getId();
    }
}
