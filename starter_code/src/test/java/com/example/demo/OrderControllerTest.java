package com.example.demo;


import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Aaron
 * @date 4/12/22 5:28 PM
 */
public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);


    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit_happy_path() {
        String username = "test";
        User user = createUser(username);
        when(userRepository.findByUsername(username)).thenReturn(user);

        ResponseEntity<UserOrder> responseEntity = orderController.submit(username);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        UserOrder userOrder = responseEntity.getBody();
        assertNotNull(userOrder);
        assertEquals(user, userOrder.getUser());
    }

    @Test
    public void submit_unhappy_path() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<UserOrder> responseEntity = orderController.submit(username);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUser_happy_path() {
        String username = "test";
        User user = createUser(username);
        List<UserOrder> userOrderList = createList();

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(userOrderList);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(username);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        List<UserOrder> userOrderList1 = responseEntity.getBody();
        assertEquals(userOrderList, userOrderList1);
    }

    @Test
    public void getOrdersForUser_unhappy_path() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(username);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
    }

    private static List<UserOrder> createList() {
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(createUser("test"));
        userOrder.setId(1L);
        userOrder.setTotal(BigDecimal.valueOf(2.99));
        userOrder.setItems(createItem());
        return Arrays.asList(userOrder);
    }

    private static User createUser(String username) {
        User user = new User();
        user.setId(0L);
        user.setUsername(username);
        user.setPassword("HashedTestPassword");
        user.setCart(createCart(user));
        return user;
    }

    private static Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(createItem());
        cart.setTotal(BigDecimal.valueOf(2.99));
        return cart;
    }

    private static List<Item> createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");
        return Arrays.asList(item);
    }
}
