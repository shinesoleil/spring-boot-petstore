package com.thoughtworks.petstore.core.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@EqualsAndHashCode(of = "username")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    @Id
    private String username;

    private UserType userType;

    @JsonIgnore
    private String password;
    private Email email;

    private User(String username, Email email, String password, UserType userType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
    }

    public static User admin(String username, Email email, String password) {
        return new User(username, email, password, UserType.ADMIN);
    }

    public static User customer(String username, Email email, String password) {
        return new User(username, email, password, UserType.CUSTOMER);
    }
}
