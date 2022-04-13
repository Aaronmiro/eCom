package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Aaron
 * @date 4/12/22 12:42 PM
 */
public class CartControllerTest {

    private CartController cartController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCart_happy_path() {
        ModifyCartRequest request = createRequest();
        User user = createUser();
        Optional<Item> item = createItem();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(user);
        when(itemRepository.findById(request.getItemId())).thenReturn(item);

        ResponseEntity<Cart> cartResponseEntity = cartController.addToCart(request);
        assertNotNull(cartResponseEntity);
        assertEquals(HttpStatus.OK.value(), cartResponseEntity.getStatusCodeValue());
        Cart cart = cartResponseEntity.getBody();
        assertNotNull(cart);

        assertEquals(item.get().getPrice(), cart.getTotal());
        assertEquals(user, cart.getUser());
        assertEquals(Arrays.asList(item.get()), cart.getItems());
    }

    @Test
    public void addToCart_unhappy_path() {
        ModifyCartRequest request = createRequest();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(null);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        ResponseEntity<Cart> cartResponseEntity = cartController.addToCart(modifyCartRequest);
        assertNotNull(cartResponseEntity);
        assertEquals(HttpStatus.NOT_FOUND.value(), cartResponseEntity.getStatusCodeValue());
    }


    @Test
    public void removeFromCart_happy_path() {
        ModifyCartRequest request = createRequest();
        User user = createUser();
        Optional<Item> item = createItem();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(user);
        when(itemRepository.findById(request.getItemId())).thenReturn(item);

        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromCart(request);
        assertNotNull(cartResponseEntity);
        assertEquals(HttpStatus.OK.value(), cartResponseEntity.getStatusCodeValue());
        Cart cart = cartResponseEntity.getBody();
        assertNotNull(cart);
    }


    @Test
    public void removeFromCart_unhappy_path() {
        ModifyCartRequest request = createRequest();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(null);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromCart(modifyCartRequest);
        assertNotNull(cartResponseEntity);
        assertEquals(HttpStatus.NOT_FOUND.value(), cartResponseEntity.getStatusCodeValue());

    }

    private static ModifyCartRequest createRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1);
        return modifyCartRequest;
    }

    private static Optional<Item> createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");
        return Optional.of(item);
    }


    private static User createUser() {
        User user = new User();
        user.setId(0L);
        user.setUsername("test");
        user.setPassword("HashedTestPassword");
        user.setCart(createCart(user));
        return user;
    }

    private static Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        return cart;
    }
}
