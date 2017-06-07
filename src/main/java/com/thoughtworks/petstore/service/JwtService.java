package com.thoughtworks.petstore.service;

import com.thoughtworks.petstore.core.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {
    private String secret;

    @Autowired
    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String getUsername(String authToken) {
        if (authToken == null) {
            return null;
        }

        try {
            return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(authToken).getBody().getSubject();
        } catch (SignatureException e) {
            return null;
        }
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<String, Object>() {{
            put("sub", user.getUsername());
            put("email", user.getEmail());
        }};

        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
    }
}
