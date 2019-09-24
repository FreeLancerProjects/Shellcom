package com.appzone.shelcom.models;

import java.io.Serializable;

public class ShippingOrderDetailsModel implements Serializable {

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
        private String num_of_tran;
        private String phone_code_from;
        private String phone_from;
        private String company_name_from;
        private String responsible_from;
        private String email_from;
        private String address_from;
        private String lat_from;
        private String long_from;
        private String load_date_from;
        private String num_of_shipping;
        private String phone_code_to;
        private String phone_to;
        private String company_name_to;
        private String responsible_to;
        private String address_to;
        private String email_to;
        private String lat_to;
        private String long_to;
        private String value;
        private String payment_method;
        private String description;
        private String image1;
        private String image2;
        private String ar_city_title_from;
        private String en_city_title_from;
        private String ar_city_title_to;
        private String en_city_title_to;
        private String ar_container_title;
        private String en_container_title;
        private String ar_title_load;
        private String en_title_load;
        private String ar_title_size;
        private String en_title_size;
        private String weight;
        private String date_from;
        private String date_to;

        public int getId() {
            return id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public String getNum_of_tran() {
            return num_of_tran;
        }

        public String getPhone_code_from() {
            return phone_code_from;
        }

        public String getPhone_from() {
            return phone_from;
        }

        public String getCompany_name_from() {
            return company_name_from;
        }

        public String getResponsible_from() {
            return responsible_from;
        }

        public String getEmail_from() {
            return email_from;
        }

        public String getAddress_from() {
            return address_from;
        }

        public String getLat_from() {
            return lat_from;
        }

        public String getLong_from() {
            return long_from;
        }

        public String getLoad_date_from() {
            return load_date_from;
        }

        public String getNum_of_shipping() {
            return num_of_shipping;
        }

        public String getPhone_code_to() {
            return phone_code_to;
        }

        public String getPhone_to() {
            return phone_to;
        }

        public String getCompany_name_to() {
            return company_name_to;
        }

        public String getResponsible_to() {
            return responsible_to;
        }

        public String getAddress_to() {
            return address_to;
        }

        public String getEmail_to() {
            return email_to;
        }

        public String getLat_to() {
            return lat_to;
        }

        public String getLong_to() {
            return long_to;
        }

        public String getValue() {
            return value;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public String getDescription() {
            return description;
        }

        public String getImage1() {
            return image1;
        }

        public String getImage2() {
            return image2;
        }

        public String getAr_city_title_from() {
            return ar_city_title_from;
        }

        public String getEn_city_title_from() {
            return en_city_title_from;
        }

        public String getAr_city_title_to() {
            return ar_city_title_to;
        }

        public String getEn_city_title_to() {
            return en_city_title_to;
        }

        public String getAr_container_title() {
            return ar_container_title;
        }

        public String getEn_container_title() {
            return en_container_title;
        }

        public String getAr_title_load() {
            return ar_title_load;
        }

        public String getEn_title_load() {
            return en_title_load;
        }

        public String getAr_title_size() {
            return ar_title_size;
        }

        public String getEn_title_size() {
            return en_title_size;
        }

        public String getWeight() {
            return weight;
        }

        public String getDate_from() {
            return date_from;
        }

        public String getDate_to() {
            return date_to;
        }
    }
}
