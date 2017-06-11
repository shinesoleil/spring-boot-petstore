package com.thoughtworks.petstore.core.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
public class Email {
    @Column(name = "email")
    private String value;

    public Email(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
