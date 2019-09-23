package com.creativeshare.shell_com.models;

import java.io.Serializable;
import java.util.List;

public class ContainersModel implements Serializable {

    private String id;
    private String ar_title_cat;
    private String en_title_cat;
    private List<Trans> trans;

    public String getId() {
        return id;
    }

    public String getAr_title_cat() {
        return ar_title_cat;
    }

    public String getEn_title_cat() {
        return en_title_cat;
    }

    public List<Trans> getTrans() {
        return trans;
    }

    public class Trans implements Serializable
    {
        private String id;
        private String en_title;
        private String ar_title;
        private String trans_image;
        private String trans_id_cat;
        private List<Loads> loads;

        public String getId() {
            return id;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getTrans_image() {
            return trans_image;
        }

        public String getTrans_id_cat() {
            return trans_id_cat;
        }

        public List<Loads> getLoads() {
            return loads;
        }
    }

    public class Loads implements Serializable
    {
        private String id;
        private String ar_title_load;
        private String en_title_load;
        private String trans_id_fk;
        private String trans_id_cat;
        private List<Sizes> sizes;

        public String getId() {
            return id;
        }

        public String getAr_title_load() {
            return ar_title_load;
        }

        public String getEn_title_load() {
            return en_title_load;
        }

        public String getTrans_id_fk() {
            return trans_id_fk;
        }

        public String getTrans_id_cat() {
            return trans_id_cat;
        }

        public List<Sizes> getSizes() {
            return sizes;
        }
    }

    public class Sizes implements Serializable
    {
        private String id;
        private String ar_title_size;
        private String en_title_size;
        private String trans_load_id_fk;


        public String getId() {
            return id;
        }

        public String getAr_title_size() {
            return ar_title_size;
        }

        public String getEn_title_size() {
            return en_title_size;
        }

        public String getTrans_load_id_fk() {
            return trans_load_id_fk;
        }
    }
}
