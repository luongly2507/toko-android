package com.app.toko.services;

import com.app.toko.models.City;
import com.app.toko.models.District;
import com.app.toko.models.Wards;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AddressService {
    @GET("tinh_tp.json")
    Call<Map<String, City>> getCities();

    @GET("quan_huyen.json")
    Call<Map<String, District>> getDistricts();

    @GET("xa_phuong.json")
    Call<Map<String, Wards>> getWards();

    @GET("quan-huyen/{id}.json")
    Call<Map<String, District>> getDistrictsDetail(@Path("id") String CityCode);

    @GET("xa-phuong/{id}.json")
    Call<Map<String, Wards>> getWardsDetial(@Path("id") String DistrictCode);

}
