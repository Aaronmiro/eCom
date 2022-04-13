package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Aaron
 * @date 4/12/22 5:27 PM
 */
public class ItemControllerTest {

    private ItemController itemController = mock(ItemController.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItems_happy_path() {
        Optional<Item> optionalItem = createItem();
        List<Item> itemList = Arrays.asList(optionalItem.get());
        when(itemRepository.findAll()).thenReturn(itemList);

        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        List<Item> resultItemList = responseEntity.getBody();
        assertNotNull(resultItemList);
        assertEquals(itemList, resultItemList);
    }

    @Test
    public void getItemById_happy_path() {
        Optional<Item> optionalItem = createItem();
        when(itemRepository.findById(1L)).thenReturn(optionalItem);

        ResponseEntity<Item> itemResponseEntity = itemController.getItemById(1L);
        assertNotNull(itemResponseEntity);
        assertEquals(HttpStatus.OK.value(), itemResponseEntity.getStatusCodeValue());
        Item item = itemResponseEntity.getBody();
        assertNotNull(item);
        assertEquals(optionalItem.get(), item);
    }

    @Test
    public void getItemsByName_happy_path() {
        Optional<Item> optionalItem = createItem();
        List<Item> itemList = Arrays.asList(optionalItem.get());

        when(itemRepository.findByName("Round Widget")).thenReturn(itemList);

        ResponseEntity<List<Item>> listResponseEntity = itemController.getItemsByName("Round Widget");
        assertNotNull(listResponseEntity);
        assertEquals(HttpStatus.OK.value(), listResponseEntity.getStatusCodeValue());
        List<Item> resultItemList = listResponseEntity.getBody();
        assertEquals(itemList, resultItemList);
    }

    private static Optional<Item> createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");
        return Optional.of(item);
    }

}
