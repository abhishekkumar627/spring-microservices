package com.abhishek.OrderService.service;

import com.abhishek.OrderService.entity.OrderEntity;
import com.abhishek.OrderService.exception.CustomException;
import com.abhishek.OrderService.external.client.PaymentService;
import com.abhishek.OrderService.external.client.ProductService;
import com.abhishek.OrderService.external.request.PaymentRequest;
import com.abhishek.OrderService.external.response.PaymentResponse;
import com.abhishek.OrderService.external.response.ProductResponse;
import com.abhishek.OrderService.model.OrderRequest;
import com.abhishek.OrderService.model.OrderResponse;
import com.abhishek.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Long placeOrder(OrderRequest orderRequest) {
        // Order Entity - Save the data with status order created
        // Product Service - Block our products(Reduce the quantity)
        // Payments Service - Payments - Success- COMPLETE OTHERWISE CANCELLED.
        log.info("orderRequest Object : {}", orderRequest);

        log.info("Reducing quantity first before placing order ");

        productService.reduceProductQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("placing order with quantity requsted : {} ", orderRequest.getQuantity());
        OrderEntity order = OrderEntity.builder().productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity()).orderDate(Instant.now()).orderStatus("CREATED")
                .amount(orderRequest.getTotalAmount()).build();
        order = orderRepository.save(order);

        log.info("Calling Payment Service to complete the payment ");
        PaymentRequest paymentRequest = PaymentRequest.builder().orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode()).amount(orderRequest.getTotalAmount()).build();
        String orderStatus = null;
        try {
            log.info("Trying to process payment with paymentRequest : {} ", paymentRequest);
            paymentService.doPayment(paymentRequest);
            orderStatus = "PLACED";
            log.info("Payment completed with orderStatus : {}", orderStatus);
        } catch (Exception e) {
            orderStatus = "PAYMENT_FAILED";
            log.error("Payment failed with orderStatus : {}", orderStatus);
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        log.info("Order placed successfully with order id : {}", order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(Long orderId) {
        log.info("Getting order details with order ID : {}", orderId);
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException("Order with specific id not found " + orderId, "NOT_FOUND", 404));
        log.info("Getting product details with order ID : {}", orderId);
        ProductResponse productResponse = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/getProduct/" + order.getProductId(), ProductResponse.class);
        log.info("Received Product Response details with product ID : {}", order.getProductId());

        log.info("Getting payment details with order ID : {}", orderId);
        PaymentResponse paymentResponse = restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/" + orderId,
                PaymentResponse.class);

        log.info("Received payment Response details with order ID : {}", orderId);

        OrderResponse orderResponse = OrderResponse.builder().orderStatus(order.getOrderStatus()).orderId(order.getId())
                .amount(order.getAmount()).orderDate(order.getOrderDate()).build();
        orderResponse.setProductDetails(productResponse);
        orderResponse.setPaymentResponse(paymentResponse);
        return orderResponse;
    }
}
