package com.creativeshare.shell_com.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private User user;

    public class User implements Serializable
    {
        private int id;
        private String email;
        private String username;
        private String phone_code;
        private String phone;
        private String logo;
        private Company company_information;

        public int getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getUsername() {
            return username;
        }

        public String getPhone_code() {
            return phone_code;
        }

        public String getPhone() {
            return phone;
        }

        public String getLogo() {
            return logo;
        }

        public Company getCompany_information() {
            return company_information;
        }
    }

    public class Company implements Serializable
    {
        private int id;
        private String company_logo;
        private String title;
        private String company_information;
        private String company_email;
        private String latitude;
        private String longitude;
        private String average_rate;
        private String city;
        private String address;
        private int is_avaliable;

        public int getId() {
            return id;
        }

        public String getCompany_logo() {
            return company_logo;
        }

        public String getTitle() {
            return title;
        }

        public String getCompany_information() {
            return company_information;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getAverage_rate() {
            return average_rate;
        }

        public String getCompany_email() {
            return company_email;
        }

        public String getCity() {
            return city;
        }

        public String getAddress() {
            return address;
        }

        public int getIs_avaliable() {
            return is_avaliable;
        }

        public void setIs_avaliable(int is_avaliable) {
            this.is_avaliable = is_avaliable;
        }
    }


    public class  CompanyServiceModel implements Serializable
    {
        private int id;
        private String user_id;
        private String company_id;
        private String service_id;
        private ServiceInformation service_information;

        public int getId() {
            return id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getCompany_id() {
            return company_id;
        }

        public String getService_id() {
            return service_id;
        }

        public ServiceInformation getService_information() {
            return service_information;
        }
    }


    public class ServiceInformation implements Serializable
    {
        private int id;
        private String ar_title;
        private String en_title;
        private String description;

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

    public List<CompanyServiceModel> getCompany_services() {
        return company_services;
    }

    public List<CompanyServiceModel> company_services;

    public User getUser() {
        return user;
    }
}
