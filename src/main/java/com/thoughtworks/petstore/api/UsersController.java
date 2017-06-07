package com.thoughtworks.petstore.api;

import com.thoughtworks.petstore.api.exception.InvalidRequestException;
import com.thoughtworks.petstore.core.user.Email;
import com.thoughtworks.petstore.core.user.User;
import com.thoughtworks.petstore.core.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlRootElement;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/users")
public class UsersController {
    private UserRepository userRepository;

    @Autowired
    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = POST)
    public ResponseEntity createUser(@Valid @RequestBody UserParams createUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException("Error in create user", bindingResult);
        }

        if (userRepository.findOne(createUser.getUsername()) != null) {
            bindingResult.rejectValue("username", "DUPLICATED", "duplicated username");
            throw new InvalidRequestException("Error in create user", bindingResult);
        }
        User user = User.customer(createUser.getUsername(), new Email(createUser.getEmail()), createUser.getPassword());
        user = userRepository.save(user);
        return ResponseEntity.status(201).body(user);
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
