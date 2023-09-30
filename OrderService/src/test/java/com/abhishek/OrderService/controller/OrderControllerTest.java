package com.abhishek.OrderService.controller;

import com.abhishek.OrderService.OrderServiceConfig;
import com.abhishek.OrderService.repository.OrderRepository;
import com.abhishek.OrderService.service.OrderService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static java.nio.charset.Charset.*;
import static org.springframework.util.StreamUtils.*;


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
            .options(WireMockConfiguration.wireMockConfig().port(8080))
            .build();

    private ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    @BeforeEach
    void setup() throws IOException {
        getProductDetailsResponse();
        getPaymentDetailsResponse();
        doPayment();
        reduceProductQuantity();
    }

    private void reduceProductQuantity() {
        //PUT product/reduceQuantity/{id}
        wireMockExtension.stubFor(post(urlMatching("product/reduceQuantity/.*"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                )
        );
    }

    private void doPayment() {
        //POST payment/doPayment
        wireMockExtension.stubFor(post(urlEqualTo("payment/doPayment"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                )
        );
    }

    private void getPaymentDetailsResponse() throws IOException {
        //GET payment/order/1
        wireMockExtension.stubFor(get(urlMatching("payment/order/.*"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(copyToString(
                                        OrderControllerTest.class.getResourceAsStream("mock/GetPayment.json"),
                                        defaultCharset()
                                )
                        )
                )
        );
    }

    private void getProductDetailsResponse() throws IOException {
        // GET product/getProduct/1
        wireMockExtension.stubFor(get("/product/getProduct/1")
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(copyToString(
                                OrderControllerTest.class.getResourceAsStream("mock/GetProduct.json"),
                                defaultCharset()
                                )
                        )
                )
        );
    }

    public void test_When_PlaceOrder_DoPayment_Success(){
        //First place order
        //Get Order by Order ID FROM DB AND CHECK
        //CHECK OUTPUT
    }
}