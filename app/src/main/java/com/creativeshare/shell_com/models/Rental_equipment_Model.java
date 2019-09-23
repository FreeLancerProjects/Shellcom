package com.creativeshare.shell_com.models;

import java.io.Serializable;

public class Rental_equipment_Model implements Serializable {
   private Order_details order_details;

    public Order_details getOrder_details() {
        return order_details;
    }

    public  class Order_details implements Serializable{
       private String order_type;
                private String user_id;
           private String order_date;
              private String description;
               private int order_status;
               private String used_time;
           private String updated_at;
           private String crerated_at;
               private int id;

           public String getOrder_type() {
               return order_type;
           }

           public String getUser_id() {
               return user_id;
           }

           public String getOrder_date() {
               return order_date;
           }

           public String getDescription() {
               return description;
           }

           public int getOrder_status() {
               return order_status;
           }

        public String getUsed_time() {
            return used_time;
        }

        public String getUpdated_at() {
               return updated_at;
           }

           public String getCrerated_at() {
               return crerated_at;
           }

           public int getId() {
               return id;
           }
       }

}
