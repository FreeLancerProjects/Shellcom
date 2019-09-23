package com.creativeshare.shell_com.models;

import java.io.Serializable;

public class Engineering_Type_Model implements Serializable {

    private int id;
    private String ar_type;
    private String en_type;


    public Engineering_Type_Model(String ar_type, String en_type) {
        this.ar_type = ar_type;
        this.en_type = en_type;
    }

    public int getId() {
        return id;
    }

    public String getAr_type() {
        return ar_type;
    }

    public String getEn_type() {
        return en_type;
    }
}
