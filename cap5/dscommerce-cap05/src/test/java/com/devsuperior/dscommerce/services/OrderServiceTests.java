package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.entities.*;
import com.devsuperior.dscommerce.repositories.OrderItemRepository;
import com.devsuperior.dscommerce.repositories.OrderRepository;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscommerce.tests.OrderFactory;
import com.devsuperior.dscommerce.tests.ProductFactory;
import com.devsuperior.dscommerce.tests.UserFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;
    private Long existingOrderId, nonExistingOrderId, existingProductId, nonExistingProductId;
    private Order order;
    private OrderDTO orderDTO;
    private User admin, client;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {

    existingOrderId = 1L;
    nonExistingOrderId = 2L;

    existingProductId = 1L;
    nonExistingProductId = 2L;

    admin = UserFactory.createCustomAdminUser(1L, "Jef");
    client = UserFactory.createCustomClientUser(2L, "Bob");

    order = OrderFactory.createOrder(client);

    orderDTO = new OrderDTO(order);

    product = ProductFactory.createProduct();

        Mockito.when(orderRepository.findById(existingOrderId)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

        Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(orderRepository.save(any())).thenReturn(order);
        Mockito.when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));

    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAdminLogged() {
        Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

        OrderDTO result = orderService.findById(existingOrderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsSelfClientLogged() {
        Mockito.doNothing().when(authService).validateSelfOrAdmin(anyLong());

        OrderDTO result = orderService.findById(existingOrderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);
    }

    @Test
    public void findByIdShouldReturnForbiddenExceptionWhenIdExistsAndOtherClientLogged() {

        Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(anyLong());

        Assertions.assertThrows(ForbiddenException.class, () -> {
            orderService.findById(existingOrderId);
        });
    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() {
        Mockito.doNothing().when(authService).validateSelfOrAdmin(anyLong());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            orderService.findById(nonExistingOrderId);
        });
    }

    @Test
    public void insertShouldReturnOrderDTOWhenAdminLogged() {
        Mockito.when(userService.authenticated()).thenReturn(admin);

        OrderDTO result = orderService.insert(orderDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);
    }

    @Test
    public void insertShouldReturnOrderDTOWhenClientLogged() {
        Mockito.when(userService.authenticated()).thenReturn(client);

        OrderDTO result = orderService.insert(orderDTO);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getClient());
        Assertions.assertEquals(result.getStatus(), OrderStatus.WAITING_PAYMENT);
        Assertions.assertNotNull(result.getMoment());
    }

    @Test
    public void insertShouldReturnThrowsUsernameNotFoundExceptionWhenUserNotLogged() {
        Mockito.doThrow(UsernameNotFoundException.class).when(userService).authenticated();

        order.setClient(new User());
        orderDTO = new OrderDTO(order);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
           OrderDTO result = orderService.insert(orderDTO);
        });
    }

    @Test
    public void insertShouldReturnEntityNotFoundExceptionWhenOrderProductIdDoesNotExist() {
        Mockito.when(userService.authenticated()).thenReturn(client);

        product.setId(nonExistingProductId);
        OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
        order.getItems().add(orderItem);

        orderDTO = new OrderDTO(order);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            OrderDTO result = orderService.insert(orderDTO);
        });
    }
}
