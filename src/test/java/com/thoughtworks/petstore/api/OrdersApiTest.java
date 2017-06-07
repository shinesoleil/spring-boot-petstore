package com.thoughtworks.petstore.api;

import com.thoughtworks.petstore.core.order.LineItem;
import com.thoughtworks.petstore.core.order.Order;
import com.thoughtworks.petstore.core.order.OrderRepository;
import com.thoughtworks.petstore.core.pet.Pet;
import com.thoughtworks.petstore.core.pet.PetRepository;
import com.thoughtworks.petstore.core.user.Email;
import com.thoughtworks.petstore.core.user.User;
import com.thoughtworks.petstore.core.user.UserRepository;
import io.restassured.RestAssured;
import org.joda.money.CurrencyUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class OrdersApiTest {
    @LocalServerPort
    private int port;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PetRepository petRepository;

    private Pet cat;
    private Pet doggy;
    private User customer;

    @Before
    public void setUp() throws Exception {
        RestAssured.port = port;

        cat = new Pet("cat", "desc", org.joda.money.Money.of(CurrencyUnit.of("CNY"), 4000), 10);
        doggy = new Pet("dog", "desc", org.joda.money.Money.of(CurrencyUnit.of("CNY"), 4000), 12);

        cat = petRepository.save(cat);
        doggy = petRepository.save(doggy);

        customer = User.customer("aisensiy", new Email("aisensiy@163.com"), "123");
        userRepository.save(customer);
    }

    @Test
    public void should_create_order_success() throws Exception {
        Map<String, Object> order = new HashMap<String, Object>() {{
            put("items", asList(
                new HashMap<String, Object>() {{
                    put("petId", cat.getId());
                    put("quantity", 1);
                }}
            ));
        }};

        given()
            .contentType("application/json")
            .body(order)
            .when()
            .post("/users/{userId}/orders", customer.getUsername())
            .then()
            .statusCode(201);
    }

    @Test
    public void should_get_one_order_success() throws Exception {
        Order order = new Order(customer.getUsername(), asList(
            new LineItem(doggy.getId(), 1, doggy.getPrice())
        ));

        order = orderRepository.save(order);

        given()
            .when()
            .get("/users/{userId}/orders/{orderId}", customer.getUsername(), order.getId())
            .then()
            .statusCode(200)
            .body("username", equalTo("aisensiy"))
            .body("items[0].price", equalTo(order.getLineItems().get(0).getPrice().toString()))
            .body("items[0].quantity", equalTo(1))
            .body("price", equalTo(order.getPrice().toString()));
    }
}
