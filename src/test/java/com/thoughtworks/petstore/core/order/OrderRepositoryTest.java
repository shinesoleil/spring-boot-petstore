package com.thoughtworks.petstore.core.order;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

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
            new LineItem(1L, "doggy", 2, org.joda.money.Money.of(CurrencyUnit.of("CNY"), 3000))
        ));
        order = orderRepository.save(order);
        assertThat(order.getId(), notNullValue());
        assertThat(order.getPrice(), is(Money.of(CurrencyUnit.of("CNY"), 6000)));

        Order fetchedOrder = orderRepository.findOne(order.getId());
        assertThat(fetchedOrder, notNullValue());
    }
}
