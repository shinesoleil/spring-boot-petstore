package com.thoughtworks.petstore.core.pet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Pet {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Money price;
    private int quantity;
    private String pictureUrl;

    public Pet(String name, String description, String pictureUrl, Money price, int quantity) {
        this.name = name;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.price = price;
        this.quantity = quantity;
    }
}
