package com.thoughtworks.petstore.api;

import com.thoughtworks.petstore.api.exception.InvalidRequestException;
import com.thoughtworks.petstore.api.exception.ResourceNotFoundException;
import com.thoughtworks.petstore.core.order.LineItem;
import com.thoughtworks.petstore.core.order.Order;
import com.thoughtworks.petstore.core.order.OrderRepository;
import com.thoughtworks.petstore.core.pet.Pet;
import com.thoughtworks.petstore.core.pet.PetRepository;
import com.thoughtworks.petstore.core.user.User;
import com.thoughtworks.petstore.core.user.UserRepository;
import lombok.Getter;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{username}/orders")
public class UserOrdersController {

    private UserRepository userRepository;

    private OrderRepository orderRepository;

    private PetRepository petRepository;

    @Autowired
    public UserOrdersController(UserRepository userRepository, OrderRepository orderRepository, PetRepository petRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.petRepository = petRepository;
    }

    @GetMapping
    public PagedResources<Order> orders(@PathVariable("username") String username, Pageable pageable) {
        User user = userRepository.findOne(username);
        if (user == null) {
            throw new ResourceNotFoundException();
        }

        Page<Order> orders = orderRepository.findByUsername(username, pageable);
        return new PagedResources<>(
            orders.getContent(),
            new PagedResources.PageMetadata(
                orders.getSize(),
                orders.getNumber(),
                orders.getTotalElements(),
                orders.getTotalPages()));
    }

    @PostMapping
    public ResponseEntity createOrder(@PathVariable("username") String username,
                                      @Valid @RequestBody OrderParam orderParam,
                                      BindingResult bindingResult) {
        User user = userRepository.findOne(username);
        if (user == null) {
            throw new ResourceNotFoundException();
        }

        Order order = new Order(user.getUsername(), orderParam.getItems().stream().map(lineItemParam -> {
            Pet pet = petRepository.findOne(lineItemParam.getPetId());
            if (pet == null) {
                bindingResult.rejectValue("items", "INVALID", "invalid pet id " + lineItemParam.getPetId());
                throw new InvalidRequestException("Error in create order", bindingResult);
            }
            return new LineItem(pet.getId(), lineItemParam.getQuantity(), pet.getPrice());
        }).collect(Collectors.toList()));
        orderRepository.save(order);
        return ResponseEntity.status(201).body(order);
    }
}

@Getter
class OrderParam {
    private List<LineItemParam> items;

    @Getter
    public static class LineItemParam {
        private int quantity;
        private long petId;
    }
}
