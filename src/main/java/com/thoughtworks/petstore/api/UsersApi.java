package com.thoughtworks.petstore.api;

import com.thoughtworks.petstore.core.order.OrderRepository;
import com.thoughtworks.petstore.core.pet.PetRepository;
import com.thoughtworks.petstore.core.user.Email;
import com.thoughtworks.petstore.core.user.User;
import com.thoughtworks.petstore.core.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

@Path("users")
@Component
public class UsersApi {
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private PetRepository petRepository;

    @Autowired
    public UsersApi(UserRepository userRepository, OrderRepository orderRepository, PetRepository petRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.petRepository = petRepository;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid UserParams userParams) {
       User user = User.customer(userParams.getUsername(), new Email(userParams.getEmail()), userParams.getPassword());
       user = userRepository.save(user);
       return Response.status(201).entity(user).build();
    }

    @Path("{userId}")
    public UserApi getUser(@PathParam("userId") String userId) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            throw new NotFoundException();
        } else {
            return new UserApi(user, orderRepository);
        }
    }
}

@Getter
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
class UserParams {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @org.hibernate.validator.constraints.Email
    private String email;
}
