package com.thoughtworks.petstore.api;

import com.thoughtworks.petstore.api.exception.ResourceNotFoundException;
import com.thoughtworks.petstore.core.order.Order;
import com.thoughtworks.petstore.core.order.OrderRepository;
import com.thoughtworks.petstore.core.user.User;
import com.thoughtworks.petstore.core.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{username}/orders/{orderId}")
public class UserOrderController {
    private UserRepository userRepository;
    private OrderRepository orderRepository;

    @Autowired
    public UserOrderController(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public ResponseEntity getOrder(@PathVariable("username") String username,
                                   @PathVariable("orderId") String orderId) {
        User user = userRepository.findOne(username);
        if (user == null) {
            throw new ResourceNotFoundException();
        }

        Order order = orderRepository.findOne(orderId);
        if (order == null) {
            throw new ResourceNotFoundException();
        }

        return ResponseEntity.ok(new Resource<Order>(order));
    }
}
