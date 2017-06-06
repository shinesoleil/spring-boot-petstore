package com.thoughtworks.petstore.api;

import com.thoughtworks.petstore.core.order.OrderRepository;
import com.thoughtworks.petstore.core.user.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class UserApi {
    private final User user;
    private final OrderRepository orderRepository;

    public UserApi(User user, OrderRepository orderRepository) {

        this.user = user;
        this.orderRepository = orderRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User user() {
        return user;
    }

    @Path("orders")
    public UserOrdersApi userOrdersApi() {
        return new UserOrdersApi(user, orderRepository);
    }
}
