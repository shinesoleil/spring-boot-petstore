package com.thoughtworks.petstore.api;

import com.thoughtworks.petstore.core.order.Order;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class UserOrderApi {
    private Order order;

    public UserOrderApi(Order order) {

        this.order = order;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Order getOrder() {
        return order;
    }
}
