package com.thoughtworks.petstore.core.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@EqualsAndHashCode(of = "username")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    @Id
    private String username;

    @JsonIgnore
    private String password;
    private Email email;

    public User(String username, Email email, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
