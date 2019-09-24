package com.appzone.shelcom.models;

import java.io.Serializable;
import java.util.List;

public class NotificationDataModel implements Serializable {

    private List<NotificationModel> data;
    private Meta meta;

    public List<NotificationModel> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }

    public class NotificationModel implements Serializable
    {
        private int id;
        private String order_id;
        private String offer_id;
        private String user_id;
        private String company_id;
        private String order_type;
        private String order_status;
        private String offer_status;
        private String notfication_type;
        private String action_type;
        private String not_date;
        private String not_status;
        private String from_name;
        private String offer_price;


        public int getId() {
            return id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getCompany_id() {
            return company_id;
        }

        public String getOrder_type() {
            return order_type;
        }

        public String getOrder_status() {
            return order_status;
        }

        public String getOffer_id() {
            return offer_id;
        }

        public String getOffer_status() {
            return offer_status;
        }

        public String getNot_date() {
            return not_date;
        }

        public String getNot_status() {
            return not_status;
        }

        public String getFrom_name() {
            return from_name;
        }

        public String getOffer_price() {
            return offer_price;
        }

        public String getNotfication_type() {
            return notfication_type;
        }

        public String getAction_type() {
            return action_type;
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
