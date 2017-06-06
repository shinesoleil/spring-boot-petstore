package com.thoughtworks.petstore.core.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thoughtworks.petstore.JacksonCustomizations;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineItem {
    @Id
    @GeneratedValue
    private Long id;

    private Long petId;
    private int quantity;

    private MonetaryAmount price;

    public LineItem(Long petId, int quantity, MonetaryAmount price) {
        this.petId = petId;
        this.quantity = quantity;
        this.price = price;
    }
}

