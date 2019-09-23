package com.creativeshare.shell_com.models;

import java.io.Serializable;

public class ShipmentUploadModel implements Serializable {
    private int truck_id;
    private String shipment_type;
    private int truck_amount;
    private String truck_size;

    private String from_company_phone_code;
    private String from_company_phone;
    private String from_company_name;
    private String from_company_email;
    private String from_responsible_name;
    private String from_address;
    private String from_city_id;
    private String shipment_number;
    private double from_lat;
    private double from_lng;
    private long from_date;

    private String to_company_phone_code;
    private String to_company_phone;
    private String to_company_name;
    private String to_company_email;
    private String to_responsible_name;
    private String to_city_id;
    private String to_address;
    private double to_lat;
    private double to_lng;
    private long to_date;

    private String load_description;
    private String load_value;
    private String load_weight;
    private String uri_1;
    private String uri_2;
    private int payment_method;


    public int getTruck_id() {
        return truck_id;
    }

    public void setTruck_id(int truck_id) {
        this.truck_id = truck_id;
    }

    public String getShipment_type() {
        return shipment_type;
    }

    public void setShipment_type(String shipment_type) {
        this.shipment_type = shipment_type;
    }

    public int getTruck_amount() {
        return truck_amount;
    }

    public void setTruck_amount(int truck_amount) {
        this.truck_amount = truck_amount;
    }

    public String getTruck_size() {
        return truck_size;
    }

    public void setTruck_size(String truck_size) {
        this.truck_size = truck_size;
    }

    public String getFrom_company_phone_code() {
        return from_company_phone_code;
    }

    public void setFrom_company_phone_code(String from_company_phone_code) {
        this.from_company_phone_code = from_company_phone_code;
    }

    public String getFrom_company_phone() {
        return from_company_phone;
    }

    public void setFrom_company_phone(String from_company_phone) {
        this.from_company_phone = from_company_phone;
    }

    public String getFrom_company_name() {
        return from_company_name;
    }

    public void setFrom_company_name(String from_company_name) {
        this.from_company_name = from_company_name;
    }

    public String getFrom_company_email() {
        return from_company_email;
    }

    public void setFrom_company_email(String from_company_email) {
        this.from_company_email = from_company_email;
    }

    public String getFrom_responsible_name() {
        return from_responsible_name;
    }

    public void setFrom_responsible_name(String from_responsible_name) {
        this.from_responsible_name = from_responsible_name;
    }

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getFrom_city_id() {
        return from_city_id;
    }

    public void setFrom_city_id(String from_city_id) {
        this.from_city_id = from_city_id;
    }

    public String getShipment_number() {
        return shipment_number;
    }

    public void setShipment_number(String shipment_number) {
        this.shipment_number = shipment_number;
    }

    public double getFrom_lat() {
        return from_lat;
    }

    public void setFrom_lat(double from_lat) {
        this.from_lat = from_lat;
    }

    public double getFrom_lng() {
        return from_lng;
    }

    public void setFrom_lng(double from_lng) {
        this.from_lng = from_lng;
    }

    public long getFrom_date() {
        return from_date;
    }

    public void setFrom_date(long from_date) {
        this.from_date = from_date;
    }

    public String getTo_company_phone_code() {
        return to_company_phone_code;
    }

    public void setTo_company_phone_code(String to_company_phone_code) {
        this.to_company_phone_code = to_company_phone_code;
    }

    public String getTo_company_phone() {
        return to_company_phone;
    }

    public void setTo_company_phone(String to_company_phone) {
        this.to_company_phone = to_company_phone;
    }

    public String getTo_company_name() {
        return to_company_name;
    }

    public void setTo_company_name(String to_company_name) {
        this.to_company_name = to_company_name;
    }

    public String getTo_company_email() {
        return to_company_email;
    }

    public void setTo_company_email(String to_company_email) {
        this.to_company_email = to_company_email;
    }

    public String getTo_responsible_name() {
        return to_responsible_name;
    }

    public void setTo_responsible_name(String to_responsible_name) {
        this.to_responsible_name = to_responsible_name;
    }

    public String getTo_city_id() {
        return to_city_id;
    }

    public void setTo_city_id(String to_city_id) {
        this.to_city_id = to_city_id;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public double getTo_lat() {
        return to_lat;
    }

    public void setTo_lat(double to_lat) {
        this.to_lat = to_lat;
    }

    public double getTo_lng() {
        return to_lng;
    }

    public void setTo_lng(double to_lng) {
        this.to_lng = to_lng;
    }

    public long getTo_date() {
        return to_date;
    }

    public void setTo_date(long to_date) {
        this.to_date = to_date;
    }

    public String getLoad_description() {
        return load_description;
    }

    public void setLoad_description(String load_description) {
        this.load_description = load_description;
    }

    public String getLoad_value() {
        return load_value;
    }

    public void setLoad_value(String load_value) {
        this.load_value = load_value;
    }

    public String getLoad_weight() {
        return load_weight;
    }

    public void setLoad_weight(String load_weight) {
        this.load_weight = load_weight;
    }

    public String getUri_1() {
        return uri_1;
    }

    public void setUri_1(String uri_1) {
        this.uri_1 = uri_1;
    }

    public String getUri_2() {
        return uri_2;
    }

    public void setUri_2(String uri_2) {
        this.uri_2 = uri_2;
    }

    public int getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(int payment_method) {
        this.payment_method = payment_method;
    }
}
