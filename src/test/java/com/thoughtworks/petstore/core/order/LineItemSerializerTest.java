package com.thoughtworks.petstore.core.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.petstore.JacksonCustomizations;
import org.joda.money.CurrencyUnit;
import org.junit.Test;

import static org.junit.Assert.*;

public class LineItemSerializerTest {
    @Test
    public void should_get_money_success() throws Exception {
        LineItem lineItem = new LineItem(1L, 1, org.joda.money.Money.of(CurrencyUnit.of("CNY"), 4000));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(LineItem.class, JacksonCustomizations.PetStoreModules.LineItemMixin.class);
        String s = objectMapper.writeValueAsString(lineItem);
        System.out.println(s);
    }
}