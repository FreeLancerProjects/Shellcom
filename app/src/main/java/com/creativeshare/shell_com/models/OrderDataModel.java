package com.creativeshare.shell_com.models;

import java.io.Serializable;
import java.util.List;

public class OrderDataModel implements Serializable {

    private List<OrderModel> data;
    private Meta meta;

    public List<OrderModel> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }

    public class OrderModel implements Serializable
    {
        private int id;
        private String order_type;
        private String user_id;
        private String company_id;
        private String offer_id;
        private String order_date;
        private String order_status;
        private String to_name;
        private String from_name;
        private String offer_price;

        public int getId() {
            return id;
        }

        public String getOrder_type() {
            return order_type;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getCompany_id() {
            return company_id;
        }

        public String getOffer_id() {
            return offer_id;
        }

        public String getOrder_date() {
            return order_date;
        }

        public String getOrder_status() {
            return order_status;
        }

        public String getTo_name() {
            return to_name;
        }

        public String getFrom_name() {
            return from_name;


        }

        public String getOffer_price() {
            return offer_price;
        }
    }
    public class Meta implements Serializable
    {
        private int current_page;

        public int getCurrent_page() {
            return current_page;
        }
    }
}
