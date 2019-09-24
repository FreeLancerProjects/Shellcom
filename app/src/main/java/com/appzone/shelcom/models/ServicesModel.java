package com.appzone.shelcom.models;

import java.io.Serializable;

public class ServicesModel implements Serializable {

    private int id;
    private String ar_title;
    private String en_title;
    private String description;

    public ServicesModel(String ar_title, String en_title) {
        this.ar_title = ar_title;
        this.en_title = en_title;
    }

    public int getId() {
        return id;
    }

    public String getAr_title() {
        return ar_title;
    }

    public String getEn_title() {
        return en_title;
    }

    public String getDescription() {
        return description;
    }
}
