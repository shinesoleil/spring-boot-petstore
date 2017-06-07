package com.thoughtworks.petstore.core.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thoughtworks.petstore.JacksonCustomizations;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@ToString(exclude = "lineItems")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "the_order")
public class Order {
    @Id
    private String id;

    private DateTime orderedDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String username;

    @Column(unique = true)
    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<LineItem> lineItems = new ArrayList<>();

    public Order(String username, Collection<LineItem> items) {
        this.id = UUID.randomUUID().toString();
        this.status = Status.PAYMENT_EXPECTED;
        this.orderedDate = DateTime.now();
        this.username = username;
        this.lineItems.addAll(items);
    }

    public Money getPrice() {

        return lineItems.stream().
            map(item -> item.getPrice().multipliedBy(item.getQuantity())).
            reduce(Money::plus).orElse(Money.of(CurrencyUnit.of("CNY"), 0));
    }

    public void markPaid() {

        if (isPaid()) {
            throw new IllegalStateException("Already paid order cannot be paid again!");
        }

        this.status = Status.PAID;
    }

    public void markInPreparation() {

        if (this.status != Status.PAID) {
            throw new IllegalStateException(
                String.format("Order must be in state payed to start preparation! Current status: %s", this.status));
        }

        this.status = Status.PREPARING;
    }

    public void markDelivering() {

        if (this.status != Status.PREPARING) {
            throw new IllegalStateException(String
                .format("Cannot mark Order delivering that is currently not preparing! Current status: %s.", this.status));
        }

        this.status = Status.DELIVERING;
    }

    public void makeFinished() {

        if (this.status != Status.DELIVERING) {
            throw new IllegalStateException(
                String.format("Cannot mark Order taken that is currently not delivering! Current status: %s.", this.status));
        }

        this.status = Status.FINISHED;
    }

    public boolean isPaid() {
        return !this.status.equals(Status.PAYMENT_EXPECTED);
    }

    public boolean isDelivering() {
        return this.status.equals(Status.DELIVERING);
    }

    public boolean isFinished() {
        return this.status.equals(Status.FINISHED);
    }


    private enum Status {
        PAYMENT_EXPECTED,
        PAID,
        PREPARING,
        DELIVERING,
        FINISHED,
        CANCELLED
    }
}
