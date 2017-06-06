package com.thoughtworks.petstore.api;

import com.thoughtworks.petstore.core.order.LineItem;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderParam {
    private List<LineItemParam> items;

    @Getter
    public static class LineItemParam {
        private int quantity;
        private long petId;
    }
}
