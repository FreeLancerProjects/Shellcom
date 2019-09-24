package com.appzone.shelcom.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Equipment_Model implements Serializable {

    private int id;
    private String en_title;
    private String ar_title;
    private String created_at;
    private String updated_at;
    private String equipment_image;
    private ArrayList<All_Equipment_Sizes> all_equipment_sizes;

    public int getId() {
        return id;
    }

    public String getEn_title() {
        return en_title;
    }

    public String getAr_title() {
        return ar_title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getEquipment_image() {
        return equipment_image;
    }

    public ArrayList<All_Equipment_Sizes> getAll_equipment_sizes() {
        return all_equipment_sizes;
    }

    public class All_Equipment_Sizes implements Serializable
    {
        private int id;
        private String en_title;
        private String ar_title;
        private String   equ_id;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEqu_id() {
            return equ_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}
