package com.abhishek.OrderService.service;

import com.abhishek.OrderService.entity.OrderEntity;
import com.abhishek.OrderService.exception.CustomException;
import com.abhishek.OrderService.external.client.PaymentService;
import com.abhishek.OrderService.external.client.ProductService;
import com.abhishek.OrderService.external.response.PaymentResponse;
import com.abhishek.OrderService.external.response.ProductResponse;
import com.abhishek.OrderService.model.OrderResponse;
import com.abhishek.OrderService.model.PaymentMode;
import com.abhishek.OrderService.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderService orderService = new OrderServiceImpl();

    @DisplayName("Get Order Success Scenario")
    @Test
    void test_When_Order_Success(){
        //Mock services
        OrderEntity order = getOrderEntity();
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/getProduct/" + order.getProductId(), ProductResponse.class))
                .thenReturn(getMockProductResponse());
        when(restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/order/"+order.getId(), PaymentResponse.class))
                .thenReturn(getMockPaymentResponse());
        //Actual Method call
        OrderResponse orderResponse = orderService.getOrderDetails(Long.valueOf(1));
        //Verification internal service calls
        verify(orderRepository, times(1)).findById(anyLong());
        verify(restTemplate, times(1)).getForObject("http://PRODUCT-SERVICE/product/getProduct/" + order.getProductId(), ProductResponse.class);
        verify(restTemplate, times(1)).getForObject("http://PAYMENT-SERVICE/payment/order/"+order.getId(), PaymentResponse.class);
        //Assert the result.
        assertNotNull(order);
        assertNotNull(orderResponse);
        assertEquals(order.getId(),orderResponse.getOrderId());
    }
    @Test
    @DisplayName("Get Orders - Failure Scenario")
    void test_when_Get_Order_NOT_FOUND_then_Not_Found(){
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));
        //Actual Method call

        CustomException exception = assertThrows(CustomException.class,()->orderService.getOrderDetails(Long.valueOf(1)));

        verify(orderRepository, times(1)).findById(anyLong());

        assertEquals("NOT_FOUND",exception.getErrorCode());
        assertEquals(404,exception.getStatus());

    }

    @Test
    @DisplayName("Get Orders - Failure Scenario no productResponse")
    void test_when_Get_Order_Product_Details_not_Found(){
        OrderEntity order = getOrderEntity();
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/getProduct/" + order.getProductId(), ProductResponse.class))
                .thenReturn(null);
        when(restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/order/"+order.getId(), PaymentResponse.class))
                .thenReturn(getMockPaymentResponse());
        //Actual Method call
        OrderResponse orderResponse = orderService.getOrderDetails(Long.valueOf(1));
        verify(orderRepository, times(1)).findById(anyLong());
        verify(restTemplate, times(1)).getForObject("http://PRODUCT-SERVICE/product/getProduct/" + order.getProductId(), ProductResponse.class);
        assertNotNull(order);
        assertNotNull(orderResponse);
        assertEquals(orderResponse.getProductDetails(),null);

    }

    private PaymentResponse getMockPaymentResponse() {
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .orderId(1)
                .paymentMode(PaymentMode.CASH)
                .paymentDate(Instant.now())
                .paymentId(1)
                .paymentStatus("ACCEPTED")
                .amount(100)
                .build();
        return paymentResponse;
    }

    private ProductResponse getMockProductResponse() {
        ProductResponse productResponse = ProductResponse.builder()
                .price(100)
                .productName("Iphone")
                .productId(Long.valueOf(2))
                .quantity(Long.valueOf(200))
                .build();
        return productResponse;
    }

    private OrderEntity getOrderEntity() {
        OrderEntity order = OrderEntity.builder()
                .orderStatus("PLACED")
                .amount(100)
                .orderDate(Instant.now())
                .id(Long.valueOf(1))
                .productId(2)
                .quantity(200)
                .build();
        return order;
    }

}