package com.creativeshare.shell_com.models;

import java.io.Serializable;

public class OrderIdModel implements Serializable {

    private OrderDetailsModelId order_details;

    public OrderDetailsModelId getOrder_details() {
        return order_details;
    }

    public class OrderDetailsModelId implements Serializable
    {
        private int id;

        public int getId() {
            return id;
        }
    }
}
