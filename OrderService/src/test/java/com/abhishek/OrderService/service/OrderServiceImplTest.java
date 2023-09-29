package com.abhishek.OrderService.service;

import com.abhishek.OrderService.entity.OrderEntity;
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
        Mockito.when(orderRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(order));
        Mockito.when(restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/getProduct/" + order.getProductId(), ProductResponse.class))
                .thenReturn(getMockProductResponse());
        Mockito.when(restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/order/"+order.getId(), PaymentResponse.class))
                .thenReturn(getMockPaymentResponse());
        //Actual Method call
        OrderResponse orderResponse = orderService.getOrderDetails(Long.valueOf(1));
        //Verification internal service calls
        Mockito.verify(orderRepository,Mockito.times(1)).findById(ArgumentMatchers.anyLong());
        Mockito.verify(restTemplate,Mockito.times(1)).getForObject("http://PRODUCT-SERVICE/product/getProduct/" + order.getProductId(), ProductResponse.class);
        Mockito.verify(restTemplate,Mockito.times(1)).getForObject("http://PAYMENT-SERVICE/payment/order/"+order.getId(), PaymentResponse.class);
        //Assert the result.
        Assertions.assertNotNull(order);
        Assertions.assertNotNull(orderResponse);
        Assertions.assertEquals(order.getId(),orderResponse.getOrderId());
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