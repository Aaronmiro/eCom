package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Aaron
 * @date 4/11/22 11:29 AM
 */
public class UserControllerTest {

    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user_happy_path() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashedTestPassword");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        ResponseEntity<User> userResponseEntity = userController.createUser(createUserRequest);
        assertNotNull(userResponseEntity);
        assertEquals(200, userResponseEntity.getStatusCode().value());

        User user = userResponseEntity.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("hashedTestPassword", user.getPassword());
    }

    @Test
    public void create_user_unhappy_path() {
        CreateUserRequest createUserRequest1 = new CreateUserRequest();
        createUserRequest1.setUsername("test");
        createUserRequest1.setPassword("testP");
        createUserRequest1.setConfirmPassword("testP");

        CreateUserRequest createUserRequest2 = new CreateUserRequest();
        createUserRequest2.setUsername("test");
        createUserRequest2.setPassword("testPassword");
        createUserRequest2.setConfirmPassword("testPasword");

        ResponseEntity<User> userResponseEntity1 = userController.createUser(createUserRequest1);
        assertNotNull(userResponseEntity1);
        assertEquals(400, userResponseEntity1.getStatusCodeValue());
        assertNull(userResponseEntity1.getBody());

        ResponseEntity<User> userResponseEntity2 = userController.createUser(createUserRequest2);
        assertNotNull(userResponseEntity2);
        assertEquals(400, userResponseEntity2.getStatusCodeValue());
        assertNull(userResponseEntity2.getBody());
    }

    @Test
    public void getUser_byId_happy_path() {
        long id = 0L;
        when(userRepository.findById(id)).thenReturn(Optional.of(createUser(id, "test")));

        ResponseEntity<User> userResponseEntity = userController.findById(id);
        assertNotNull(userResponseEntity);
        assertEquals(200, userResponseEntity.getStatusCode().value());
        User user = userResponseEntity.getBody();
        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("testPassword", user.getPassword());
    }

    @Test
    public void getUser_byUsername_happy_path() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(createUser(0L, username));

        ResponseEntity<User> userResponseEntity = userController.findByUserName(username);
        assertNotNull(userResponseEntity);
        assertEquals(200, userResponseEntity.getStatusCode().value());
        User user = userResponseEntity.getBody();
        assertNotNull(user);
        assertEquals(0L, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals("testPassword", user.getPassword());
    }

    private static User createUser(Long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword("testPassword");
        return user;
    }
}
