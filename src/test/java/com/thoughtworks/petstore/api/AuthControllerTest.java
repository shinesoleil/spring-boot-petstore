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
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @MockBean
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;
    private User customer;

    @Before
    public void setUp() throws Exception {
        RestAssured.port = port;
        customer = User.customer("aisensiy", new Email("aisensiy@163.com"), "123");
        customer = userRepository.save(customer);
    }

    @Test
    public void should_login_success() throws Exception {
        Map<String, Object> loginParameter = new HashMap<String, Object>() {{
            put("username", "aisensiy");
            put("password", "124");
        }};

        when(jwtService.generateToken(any())).thenReturn("token");

        given()
            .contentType("application/json")
            .body(loginParameter)
            .when()
            .post("/auth")
            .then()
            .statusCode(200)
            .body("token", equalTo("token"));

    }

    @Test
    public void should_fail_login_with_wrong_password() throws Exception {
        Map<String, Object> loginParameter = new HashMap<String, Object>() {{
            put("username", "aisensiy");
            put("password", "456");
        }};

        given()
            .contentType("application/json")
            .body(loginParameter)
            .when()
            .post("/auth")
            .then()
            .statusCode(400);
    }

    @Test
    public void should_fail_with_wrong_parameter_format() throws Exception {
        Map<String, Object> loginParameter = new HashMap<String, Object>() {{
            put("username", "aisensiy");
            put("passwd", "123");
        }};

        given()
            .contentType("application/json")
            .body(loginParameter)
            .when()
            .post("/auth")
            .then()
            .statusCode(400);

    }
}
