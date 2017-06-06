package com.thoughtworks.petstore.api;

import com.thoughtworks.petstore.core.order.LineItem;
import com.thoughtworks.petstore.core.order.Order;
import com.thoughtworks.petstore.core.order.OrderRepository;
import com.thoughtworks.petstore.core.pet.Pet;
import com.thoughtworks.petstore.core.pet.PetRepository;
import com.thoughtworks.petstore.core.user.User;

import javax.money.MonetaryAmount;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

public class UserOrdersApi {
    private final User user;
    private OrderRepository orderRepository;

    public UserOrdersApi(User user, OrderRepository orderRepository) {
        this.user = user;
        this.orderRepository = orderRepository;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOrder(@Valid OrderParam orderParam, @Context PetRepository petRepository) {
        Order order = new Order(user.getUsername(), orderParam.getItems().stream().map(lineItemParam -> {
            Pet pet = petRepository.findOne(lineItemParam.getPetId());
            return new LineItem(pet.getId(), lineItemParam.getQuantity(), pet.getPrice());
        }).collect(Collectors.toList()));
        orderRepository.save(order);
        return Response.status(201).build();
    }

    @Path("{orderId}")
    public UserOrderApi userOrderApi(@PathParam("orderId") String orderId) {
        Order order = orderRepository.findOne(orderId);

        if (order == null) {
            throw new NotFoundException();
        } else {
            return new UserOrderApi(order);
        }
    }
}

