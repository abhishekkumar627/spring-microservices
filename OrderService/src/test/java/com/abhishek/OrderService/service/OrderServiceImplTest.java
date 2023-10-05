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
import com.abhishek.OrderService.model.PaymentMode;
import com.abhishek.OrderService.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    void test_When_Order_Success() {
        // Mock services
        OrderEntity order = getOrderEntity();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(restTemplate.getForObject("http://PRODUCT-SERVICE/product/getProduct/" + order.getProductId(),
                ProductResponse.class)).thenReturn(getMockProductResponse());
        when(restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/" + order.getId(), PaymentResponse.class))
                .thenReturn(getMockPaymentResponse());
        // Actual Method call
        OrderResponse orderResponse = orderService.getOrderDetails(Long.valueOf(1));
        // Verification internal service calls
        verify(orderRepository, times(1)).findById(anyLong());
        verify(restTemplate, times(1)).getForObject("http://PRODUCT-SERVICE/product/getProduct/" + order.getProductId(),
                ProductResponse.class);
        verify(restTemplate, times(1)).getForObject("http://PAYMENT-SERVICE/payment/order/" + order.getId(),
                PaymentResponse.class);
        // Assert the result.
        assertNotNull(order);
        assertNotNull(orderResponse);
        assertEquals(order.getId(), orderResponse.getOrderId());
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
    void test_when_Get_Order_Product_Details_not_Found() {
        OrderEntity order = getOrderEntity();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(restTemplate.getForObject("http://PRODUCT-SERVICE/product/getProduct/" + order.getProductId(),
                ProductResponse.class)).thenReturn(null);
        when(restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/" + order.getId(), PaymentResponse.class))
                .thenReturn(getMockPaymentResponse());
        // Actual Method call
        OrderResponse orderResponse = orderService.getOrderDetails(Long.valueOf(1));
        verify(orderRepository, times(1)).findById(anyLong());
        verify(restTemplate, times(1)).getForObject("http://PRODUCT-SERVICE/product/getProduct/" + order.getProductId(),
                ProductResponse.class);
        assertNotNull(order);
        assertNotNull(orderResponse);
        assertEquals(orderResponse.getProductDetails(), null);

    }

    @Test
    @DisplayName("place order - success Scenario")
    void test_When_Place_Order_Success() {
        OrderRequest orderRequest = getMockOrderRequest();
        OrderEntity order = getOrderEntity();
        when(productService.reduceProductQuantity(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(order);
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenReturn(new ResponseEntity<Long>(1L, HttpStatus.OK));
        Long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository, times(2)).save(any(OrderEntity.class));
        verify(productService, times(1)).reduceProductQuantity(anyLong(), anyLong());
        verify(paymentService, times(1)).doPayment(any(PaymentRequest.class));

        assertEquals(order.getId(), orderId);

    }

    @Test
    @DisplayName("place order - payment failure Scenario")
    void test_When_Payment_Failed_But_Order_Placed() {
        OrderRequest orderRequest = getMockOrderRequest();
        OrderEntity order = getOrderEntity();
        when(productService.reduceProductQuantity(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(order);
        when(paymentService.doPayment(any(PaymentRequest.class))).thenThrow(new RuntimeException());
        Long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository, times(2)).save(any(OrderEntity.class));
        verify(productService, times(1)).reduceProductQuantity(anyLong(), anyLong());
        verify(paymentService, times(1)).doPayment(any(PaymentRequest.class));

        assertEquals(order.getId(), orderId);
        assertEquals("PAYMENT_FAILED", order.getOrderStatus());
    }

    private OrderRequest getMockOrderRequest() {
        return OrderRequest.builder().paymentMode(PaymentMode.CASH).totalAmount(100).productId(1).quantity(10).build();
    }

    private PaymentResponse getMockPaymentResponse() {
        PaymentResponse paymentResponse = PaymentResponse.builder().orderId(1).paymentMode(PaymentMode.CASH)
                .paymentDate(Instant.now()).paymentId(1).paymentStatus("ACCEPTED").amount(100).build();
        return paymentResponse;
    }

    private ProductResponse getMockProductResponse() {
        ProductResponse productResponse = ProductResponse.builder().price(100).productName("Iphone")
                .productId(Long.valueOf(2)).quantity(Long.valueOf(200)).build();
        return productResponse;
    }

    private OrderEntity getOrderEntity() {
        OrderEntity order = OrderEntity.builder().orderStatus("PLACED").amount(100).orderDate(Instant.now())
                .id(Long.valueOf(1)).productId(2).quantity(200).build();
        return order;
    }

}