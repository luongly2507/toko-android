package com.app.toko.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor

public class Contact {
    private String id;
    private String telephone;
    private String receiver;
    private String city;
    private String district;
    private String ward;
    private String line;

    public Contact(String id, String telephone, String receiver, String city, String district, String ward, String line) {
        this.id = id;
        this.telephone = telephone;
        this.receiver = receiver;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.line = line;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id='" + id + '\'' +
                ", telephone='" + telephone + '\'' +
                ", receiver='" + receiver + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", ward='" + ward + '\'' +
                ", line='" + line + '\'' +
                '}';
    }
}
