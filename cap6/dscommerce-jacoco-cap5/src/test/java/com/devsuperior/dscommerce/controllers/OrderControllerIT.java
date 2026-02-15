package com.devsuperior.dscommerce.controllers;

import com.devsuperior.dscommerce.entities.*;
import com.devsuperior.dscommerce.tests.ProductFactory;
import com.devsuperior.dscommerce.tests.UserFactory;
import com.devsuperior.dscommerce.utils.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingOrderId, nonExistingOrderId, adminOrderId;
    private String usernameAdmin, usernameClient, password, bearerTokenAdmin, bearerTokenClient, bearerTokenInvalid;
    private User userEntity;
    private Order order;

    @BeforeEach
    void setUp() throws Exception {

        existingOrderId = 1L;
        nonExistingOrderId = 100L;
        adminOrderId = 2L;

        usernameClient = "maria@gmail.com";
        usernameAdmin = "alex@gmail.com";
        password = "123456";

        userEntity = UserFactory.createClientUser();
        order = new Order(null, Instant.now(), OrderStatus.WAITING_PAYMENT, userEntity, null);

        Product product = ProductFactory.createProduct();
        OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
        order.getItems().add(orderItem);

        bearerTokenClient = tokenUtil.obtainAccessToken(mockMvc, usernameClient, password);
        bearerTokenAdmin = tokenUtil.obtainAccessToken(mockMvc, usernameAdmin, password);
        bearerTokenInvalid = bearerTokenClient + "zkto";

    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistAndAdminLogged() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/orders/{id}", existingOrderId)
                        .header("Authorization", "Bearer " + bearerTokenAdmin)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingOrderId));
        result.andExpect(jsonPath("$.moment").isNotEmpty());
        result.andExpect(jsonPath("$.status").isNotEmpty());
        result.andExpect(jsonPath("$.client").isNotEmpty());
        result.andExpect(jsonPath("$.client.name").value("Maria Brown"));
        result.andExpect(jsonPath("$.payment").isNotEmpty());
        result.andExpect(jsonPath("$.items").isNotEmpty());
        result.andExpect(jsonPath("$.items[1].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.total").isNotEmpty());
    }

    @Test
    public void findByIdShouldReturnForbiddenWhenClientAccessOrderFromOtherClient() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/orders/{id}", adminOrderId)
                        .header("Authorization", "Bearer " + bearerTokenClient)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void findByIdShouldReturnOrderWhenClientAccessOrder() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/orders/{id}", existingOrderId)
                        .header("Authorization", "Bearer " + bearerTokenClient)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingOrderId));
        result.andExpect(jsonPath("$.moment").isNotEmpty());
        result.andExpect(jsonPath("$.status").isNotEmpty());
        result.andExpect(jsonPath("$.client").isNotEmpty());
        result.andExpect(jsonPath("$.client.name").value("Maria Brown"));
        result.andExpect(jsonPath("$.payment").isNotEmpty());
        result.andExpect(jsonPath("$.items").isNotEmpty());
        result.andExpect(jsonPath("$.items[1].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.total").isNotEmpty());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenAdminLogged() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/orders/{id}", nonExistingOrderId)
                        .header("Authorization", "Bearer " + bearerTokenAdmin)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenClientLogged() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/orders/{id}", nonExistingOrderId)
                        .header("Authorization", "Bearer " + bearerTokenClient)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnUnauthorizedWhenIsNotLogged() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/orders/{id}", existingOrderId)
                        .header("Authorization", "Bearer " + bearerTokenInvalid)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }

}
