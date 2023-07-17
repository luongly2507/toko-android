package com.app.toko.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.regex.Pattern;

public class AddressViewModel extends BaseObservable {

    private String ht;
    private String sdt;
    private String city;
    private String district;
    private String wards;
    private String address;

    private String location;

    public AddressViewModel(String ht, String sdt, String city, String district, String wards, String address, String location) {
        this.ht = ht;
        this.sdt = sdt;
        this.city = city;
        this.district = district;
        this.wards = wards;
        this.address = address;
        this.location = location;
    }


    @Bindable
    public String getHt() {
        return ht;
    }

    public void setHt(String ht) {
        this.ht = ht;
        notifyPropertyChanged(BR.ht);
    }

    @Bindable
    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
        notifyPropertyChanged(BR.sdt);
    }

    @Bindable
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
        notifyPropertyChanged(BR.district);
    }

    @Bindable
    public String getWards() {
        return wards;
    }

    public void setWards(String wards) {
        this.wards = wards;
        notifyPropertyChanged(BR.wards);
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        notifyPropertyChanged(BR.location);
    }

    public void onClickLocation(String location){
        setLocation(location);
    }

    public boolean isValidName (){
        return !TextUtils.isEmpty(ht);
    }

    public  boolean isValidPhone()
    {
        return  !TextUtils.isEmpty(sdt) && Patterns.PHONE.matcher(sdt).matches() && (sdt.length()==10);
    }

    public boolean isValidCity (){
        return !TextUtils.isEmpty(city);
    }

    public  boolean isValidDítrict()
    {
        return !TextUtils.isEmpty(district);
    }

    public  boolean isValidWards()
    {
        return !TextUtils.isEmpty(wards);
    }

    public  boolean isValidAddress()
    {
        return !TextUtils.isEmpty(address);
    }

    public  boolean isValidLocation()
    {
        return !TextUtils.isEmpty(location);
    }

}
