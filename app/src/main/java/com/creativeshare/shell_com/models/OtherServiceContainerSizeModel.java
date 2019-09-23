package com.creativeshare.shell_com.models;

import java.io.Serializable;
import java.util.List;

public class OtherServiceContainerSizeModel implements Serializable {

    private List<Size> all_containers_sizes;

    public List<Size> getAll_containers_sizes() {
        return all_containers_sizes;
    }

    public static class Size implements Serializable
    {
        private int id;
        private String containerType_id;
        private String ar_size;
        private String en_size;


        public Size(String ar_size, String en_size) {
            this.ar_size = ar_size;
            this.en_size = en_size;
        }

        public int getId() {
            return id;
        }

        public String getContainerType_id() {
            return containerType_id;
        }

        public String getAr_size() {
            return ar_size;
        }

        public String getEn_size() {
            return en_size;
        }
    }
}
