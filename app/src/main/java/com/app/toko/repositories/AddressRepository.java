package com.app.toko.repositories;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.models.City;
import com.app.toko.models.District;
import com.app.toko.models.Wards;
import com.app.toko.services.AddressService;
import com.app.toko.utils.ApiService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressRepository {
    private AddressService addressService;
    private Application application;
    private Context appContext;
    private MutableLiveData<Map<String, City>> cityMutableLiveData;
    private MutableLiveData<Map<String, District>> districtMutableLiveData;
    private MutableLiveData<Map<String, Wards>> wardsMutableLiveData;
    public AddressRepository(Application application) {
        this.addressService = ApiService.getAddressService();
        this.application = application;
        this.appContext = application.getApplicationContext();
        this.cityMutableLiveData = new MutableLiveData<>();
        this.districtMutableLiveData = new MutableLiveData<>();
        this.wardsMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<Map<String, City>> getCityLiveData() {
      return cityMutableLiveData;
    }
    public LiveData<Map<String, District>> getDistrictLiveData() {
        return districtMutableLiveData;
    }
    public LiveData<Map<String, Wards>> getWardsLiveData() {
        return wardsMutableLiveData;
    }

    public void LoadCities(){
        addressService.getCities().enqueue(new Callback<Map<String, City>>() {
            @Override
            public void onResponse(Call<Map<String, City>> call, Response<Map<String, City>> response) {
                Log.d("Address", "Get API City Success");
                if (response.isSuccessful()) {

                    Map<String, City> cities = response.body();
                    cityMutableLiveData.postValue(cities);

                } else {
                    System.out.println("Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Map<String, City>> call, Throwable t) {
                Log.d("Address", "Get API City Failure");
            }
        });
    }

    public void LoadDistricts(){
        addressService.getDistricts().enqueue(new Callback<Map<String, District>>() {
            @Override
            public void onResponse(Call<Map<String, District>> call, Response<Map<String, District>> response) {
                Log.d("Address", "Get API District Success");
                if (response.isSuccessful()) {

                    Map<String, District> districts = response.body();
                    districtMutableLiveData.postValue(districts);

                } else {
                    System.out.println("Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Map<String, District>> call, Throwable t) {
                Log.d("Address", "Get API District Failure");
            }
        });
    }

    public void LoadWards(){
        addressService.getWards().enqueue(new Callback<Map<String, Wards>>() {
            @Override
            public void onResponse(Call<Map<String, Wards>> call, Response<Map<String, Wards>> response) {
                Log.d("Address", "Get API Wards Success");
                if (response.isSuccessful()) {

                    Map<String, Wards> wards = response.body();
                    wardsMutableLiveData.postValue(wards);

                } else {
                    System.out.println("Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Wards>> call, Throwable t) {
                Log.d("Address", "Get API Wards Failure");
            }
        });
    }

    public void LoadDistrictsByCity(String CityCode){
        addressService.getDistrictsDetail(CityCode).enqueue(new Callback<Map<String, District>>() {
            @Override
            public void onResponse(Call<Map<String, District>> call, Response<Map<String, District>> response) {
                Log.d("Address", "Get API District by City Success");
                if (response.isSuccessful()) {

                    Map<String, District> districts = response.body();
                    districtMutableLiveData.postValue(districts);

                } else {
                    System.out.println("Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Map<String, District>> call, Throwable t) {
                Log.d("Address", "Get API District by City Failure");
            }
        });
    }

    public void LoadWardsByDistrict(String DistrictCode){
        addressService.getWardsDetial(DistrictCode).enqueue(new Callback<Map<String, Wards>>() {
            @Override
            public void onResponse(Call<Map<String, Wards>> call, Response<Map<String, Wards>> response) {
                Log.d("Address", "Get API Wards by District Success");
                if (response.isSuccessful()) {

                    Map<String, Wards> wards = response.body();
                    wardsMutableLiveData.postValue(wards);

                } else {
                    System.out.println("Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Wards>> call, Throwable t) {
                Log.d("Address", "Get API Wards by District Failure");
            }
        });
    }

}
