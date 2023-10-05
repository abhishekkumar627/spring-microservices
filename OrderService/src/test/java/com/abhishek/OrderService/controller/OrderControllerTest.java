package com.abhishek.OrderService.controller;

import com.abhishek.OrderService.OrderServiceConfig;
import com.abhishek.OrderService.entity.OrderEntity;
import com.abhishek.OrderService.external.response.PaymentResponse;
import com.abhishek.OrderService.external.response.ProductResponse;
import com.abhishek.OrderService.model.OrderRequest;
import com.abhishek.OrderService.model.OrderResponse;
import com.abhishek.OrderService.model.PaymentMode;
import com.abhishek.OrderService.repository.OrderRepository;
import com.abhishek.OrderService.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.charset.Charset.defaultCharset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.util.StreamUtils.copyToString;

@SpringBootTest({"server.port=0"})
@EnableConfigurationProperties
@AutoConfigureMockMvc
@ContextConfiguration(classes = {OrderServiceConfig.class})
public class OrderControllerTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MockMvc mockMvc;

    @RegisterExtension
    private WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().port(8080)).build();

    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @BeforeEach
    void setup() throws IOException {
        getProductDetailsResponse();
        doPayment();
        getPaymentDetailsResponse();
        reduceProductQuantity();
    }

    private void reduceProductQuantity() {
        // PUT product/reduceQuantity/{id}
        wireMockExtension.stubFor(put(urlMatching("/product/reduceQuantity/.*")).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value()).withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
    }

    private void doPayment() {
        // POST payment/doPayment
        wireMockExtension.stubFor(post(urlEqualTo("/payment/doPayment")).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value()).withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
    }

    private void getPaymentDetailsResponse() throws IOException {
        // GET payment/order/1
        wireMockExtension.stubFor(get(urlMatching("/payment/order/.*")).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value()).withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(copyToString(OrderControllerTest.class.getClassLoader().getResourceAsStream("mock/GetPayment.json"),
                        defaultCharset()))));
    }

    private void getProductDetailsResponse() throws IOException {
        // GET product/getProduct/1
        wireMockExtension.stubFor(get("/product/getProduct/1").willReturn(aResponse().withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE).withBody(copyToString(
                        OrderControllerTest.class.getClassLoader().getResourceAsStream("mock/GetProduct.json"), defaultCharset()))));
    }

    @Test
    public void test_When_PlaceOrder_DoPayment_Success() throws Exception {
        // First place order
        // Get Order by Order ID FROM DB AND CHECK
        // CHECK OUTPUT

        OrderRequest orderRequest = getMockOrderRequest();
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/order/placeOrder")
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String orderId = mvcResult.getResponse().getContentAsString();
        Optional<OrderEntity> order = orderRepository.findById(Long.valueOf(orderId));
        assertTrue(order.isPresent());
        OrderEntity o = order.get();
        assertEquals(Long.parseLong(orderId), o.getId());
        assertEquals("PLACED", o.getOrderStatus());
        assertEquals(orderRequest.getTotalAmount(), o.getAmount());
        assertEquals(orderRequest.getQuantity(), o.getQuantity());

    }

    private OrderRequest getMockOrderRequest() {
        return OrderRequest.builder().quantity(10).productId(1).totalAmount(200).paymentMode(PaymentMode.CASH).build();
    }

    @Test
    public void test_When_PlaceOrderWithWrongAccess_thenThrow403() throws Exception {
        // First place order
        // Get Order by Order ID FROM DB AND CHECK
        // CHECK OUTPUT

        OrderRequest orderRequest = getMockOrderRequest();
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/order/placeOrder")
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("Admin")))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn();

    }

    @Test
    public void test_WhenGetOrder_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/order/getOrderDetails/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority("Admin")))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        OrderEntity order = orderRepository.findById(1l).get();
        String expectedResponse = getOrderResponse(order);
        Assertions.assertEquals(expectedResponse,actualResponse);
    }

    private String getOrderResponse(OrderEntity order) throws IOException {
        ProductResponse productResponse
                = objectMapper.readValue(
                        copyToString(OrderControllerTest.class.getClassLoader().getResourceAsStream("mock/GetProduct.json"), defaultCharset()),
                        ProductResponse.class);
        PaymentResponse paymentResponse
                = objectMapper.readValue(
                copyToString(OrderControllerTest.class.getClassLoader().getResourceAsStream("mock/GetPayment.json"), defaultCharset()),
                PaymentResponse.class);
        OrderResponse orderResponse = OrderResponse.builder().orderStatus(order.getOrderStatus()).orderId(order.getId())
                .amount(order.getAmount()).orderDate(order.getOrderDate()).build();
        orderResponse.setProductDetails(productResponse);
        orderResponse.setPaymentResponse(paymentResponse);
        return objectMapper.writeValueAsString(orderResponse);
    }
}