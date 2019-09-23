package com.creativeshare.shell_com.models;

import java.io.Serializable;

public class ContainersOrderDetailsModel implements Serializable {

    private Order order;
    private Order_Details order_details;

    public Order getOrder() {
        return order;
    }

    public Order_Details getOrder_details() {
        return order_details;
    }

    public class Order implements Serializable
    {
        private int id;
        private String order_type;
        private String user_id;
        private String company_id;
        private String offer_id;
        private String description;
        private String order_date;
        private String order_status;
        private String from_user_id;
        private String to_user_id;
        private String from_user_name;
        private String from_user_image;
        private String to_user_name;
        private String to_user_image;
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

        public String getDescription() {
            return description;
        }

        public String getOrder_date() {
            return order_date;
        }

        public String getOrder_status() {
            return order_status;
        }

        public String getFrom_user_id() {
            return from_user_id;
        }

        public String getTo_user_id() {
            return to_user_id;
        }

        public String getFrom_user_name() {
            return from_user_name;
        }

        public String getFrom_user_image() {
            return from_user_image;
        }

        public String getTo_user_name() {
            return to_user_name;
        }

        public String getTo_user_image() {
            return to_user_image;
        }

        public String getOffer_price() {
            return offer_price;
        }
    }

    public class Order_Details implements Serializable
    {
        private int id;
        private String order_id;
        private String latitude;
        private String longitude;
        private String address;
        private String ar_type;
        private String en_type;
        private String ar_size;
        private String en_size;


        public int getId() {
            return id;
        }

        public String getOrder_id() {
            return order_id;
        }


        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getAddress() {
            return address;
        }

        public String getAr_type() {
            return ar_type;
        }

        public String getEn_type() {
            return en_type;
        }

        public String getAr_size() {
            return ar_size;
        }

        public String getEn_size() {
            return en_size;
        }
    }
}
