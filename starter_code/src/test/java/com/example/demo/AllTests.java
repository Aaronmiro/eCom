package com.example.demo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Aaron
 * @date 4/12/22 5:22 PM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CartControllerTest.class, UserControllerTest.class, ItemControllerTest.class, OrderControllerTest.class})
public class AllTests {

}
