package com.creativeshare.shell_com.models;

import java.io.Serializable;
import java.util.List;

public class SliderDataModel implements Serializable {

    private List<SliderModel> data;

    public List<SliderModel> getData() {
        return data;
    }

    public class SliderModel implements Serializable
    {
        private int id;
        private String url;

        public int getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }
    }
}
