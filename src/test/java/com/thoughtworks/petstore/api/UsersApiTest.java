package com.thoughtworks.petstore.api;

import com.thoughtworks.petstore.core.user.Email;
import com.thoughtworks.petstore.core.user.User;
import com.thoughtworks.petstore.core.user.UserRepository;
import com.thoughtworks.petstore.service.JwtService;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UsersApiTest {
    @LocalServerPort
    private int port;

    @Autowired
    UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        RestAssured.port = port;
    }

    @Test
    public void should_create_user_success() throws Exception {
        Map<String, Object> userParams = new HashMap<String, Object>() {{
            put("username", "test");
            put("password", "password");
            put("email", "aisensiy@163.com");
        }};

        given()
            .contentType("application/json")
            .body(userParams)
            .when()
            .post("/users")
            .then()
            .statusCode(201) //201
            .body("username", equalTo("test"));
    }

    @Test
    public void should_get_400_with_invalid_email() throws Exception {
        Map<String, Object> userParams = new HashMap<String, Object>() {{
            put("username", "aisensiy");
            put("password", "password");
            put("email", "invalid");
        }};

        given()
            .contentType("application/json")
            .body(userParams)
            .when()
            .post("/users")
            .then()
            .statusCode(400) //400
            .body("fieldErrors[0].field", equalTo("email"));
    }

    @Test
    public void should_get_400_with_null_username() throws Exception {
        Map<String, Object> userParams = new HashMap<String, Object>() {{
            put("password", "password");
            put("email", "aisensiy@gmail.com");
        }};

        given()
            .contentType("application/json")
            .body(userParams)
            .when()
            .post("/users")
            .then()
            .statusCode(400);
    }

    @Test
    public void should_get_one_user_success() throws Exception {
        User user = User.customer("aisensiy", new Email("aisensiy@163.com"), "123");
        userRepository.save(user);

        given()
            .when()
            .get("/users/{userId}", user.getUsername())
            .then()
            .statusCode(200)
            .body("username", equalTo(user.getUsername()))
            .body("email.value", equalTo(user.getEmail().getValue()))
            .body("type", equalTo("CUSTOMER"));
    }

    @Test
    public void new_feature_test() throws Exception {
        assertThat(true, is(true));
    }
}
