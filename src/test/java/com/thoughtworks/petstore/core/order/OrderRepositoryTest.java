package com.thoughtworks.petstore.core.order;

import org.javamoney.moneta.Money;
import org.javamoney.moneta.internal.MoneyAmountBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.money.MonetaryAmount;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void should_create_order_success() throws Exception {
        Order order = new Order("aisensiy", asList(
            new LineItem(1L, 2, Money.of(3000, "CNY"))
        ));
        order = orderRepository.save(order);
        assertThat(order.getId(), notNullValue());
        assertThat(order.getPrice(), is(Money.of(6000, "CNY")));

        Order fetchedOrder = orderRepository.findOne(order.getId());
        assertThat(fetchedOrder, notNullValue());
    }
}