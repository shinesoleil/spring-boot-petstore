package com.thoughtworks.petstore.api;

import com.thoughtworks.petstore.api.exception.InvalidRequestException;
import com.thoughtworks.petstore.core.user.User;
import com.thoughtworks.petstore.core.user.UserRepository;
import com.thoughtworks.petstore.service.JwtService;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserRepository userRepository;
    private JwtService jwtService;

    @Autowired
    public AuthController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("bad request");
        }

        User user = userRepository.findOne(authRequest.getUsername());

        if (user != null && authRequest.getPassword().endsWith(user.getPassword())) {
            String token = jwtService.generateToken(user);

            return ResponseEntity.ok(new HashMap<String, Object>() {{
                put("token", token);
            }});
        } else {
            return ResponseEntity.badRequest().body("bad request");
        }
    }
}

@Data
class AuthRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
