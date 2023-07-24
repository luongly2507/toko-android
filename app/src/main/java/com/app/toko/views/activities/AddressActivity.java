package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.app.toko.R;
import com.app.toko.databinding.ActivityAddressBinding;
import com.app.toko.models.City;
import com.app.toko.models.Contact;
import com.app.toko.models.District;
import com.app.toko.models.Wards;
import com.app.toko.viewmodels.AddressViewModel;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddressActivity extends AppCompatActivity {

    private ActivityAddressBinding mActivityAddressBinding;
    private View mView;
    private AddressViewModel addressViewModel;

    private SharedPreferences sharedPreferences;
    private String access_token;
    private String userIDStr;
    private String[] cities;
    private String[] districts;
    private String[] wards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityAddressBinding = ActivityAddressBinding.inflate(getLayoutInflater());
        mView = mActivityAddressBinding.getRoot();
        addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);

        sharedPreferences = this.getSharedPreferences("toko-preferences", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("access_token", null) == null){
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            access_token = sharedPreferences.getString("access_token" , null);
            userIDStr = sharedPreferences.getString("user_id" , null);
        }

        mActivityAddressBinding.setLifecycleOwner(this);
        mActivityAddressBinding.setAddressViewModel(addressViewModel);
        setContentView(mView);

        mActivityAddressBinding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddressActivity.this, "Back", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Đặt sự kiện onClick cho nút buttonOK
        mActivityAddressBinding.buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressViewModel.isValidName()
                        && addressViewModel.isValidPhone()
                        && addressViewModel.isValidCity()
                        && addressViewModel.isValidDistrict()
                        && addressViewModel.isValidWards()
                        && addressViewModel.isValidAddress()
                        && addressViewModel.isValidLocation()) {

                    addressViewModel.registerAddress();
                    addressViewModel.getContact().observe(AddressActivity.this, new Observer<Contact>() {
                        @Override
                        public void onChanged(Contact contact) {
                            if (contact == null) {
                                Log.d("Contact", "NULL");
                                return;
                            }
                            Toast.makeText(AddressActivity.this, "Xác Nhận", Toast.LENGTH_SHORT).show();
                            addressViewModel.getContactRepository().RegisterContact(addressViewModel.getContact(),UUID.fromString(userIDStr),"Bearer " + access_token);
                            finish();
                        }
                    });

                } else {
                    Toast.makeText(AddressActivity.this, "Không thể xác nhận", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Khi khởi tạo Activity, cần gọi phương thức loadCities() trong ViewModel để tải dữ liệu thành phố
        addressViewModel.loadCities();
        addressViewModel.getAddressRepository().getCityLiveData().observe(this, new Observer<Map<String, City>>() {
            @Override
            public void onChanged(Map<String, City> stringCityMap) {
                addressViewModel.ConvertToCities(stringCityMap);
            }
        });

        // Lắng nghe sự thay đổi của danh sách thành phố
        addressViewModel.getCities().observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                if (strings != null)
                {
                    cities = strings;
                    setupACTcities();
                }
                else
                {
                    cities = new String[0]; // Hoặc trả về mảng rỗng nếu không có dữ liệu
                }
            }
        });


        // Quận / Huyện
        addressViewModel.getAddressRepository().getDistrictLiveData().observe(this, new Observer<Map<String, District>>() {
            @Override
            public void onChanged(Map<String, District> districtMap) {
                addressViewModel.ConvertToDistricts(districtMap);
            }
        });
        addressViewModel.getDistricts().observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                if (strings != null) {
                    districts = strings;
                    setupACTdistricts();
                } else {
                    districts = new String[0];
                }
            }
        });


        // Phường / Xã
        addressViewModel.getAddressRepository().getWardsLiveData().observe(this, new Observer<Map<String, Wards>>() {
            @Override
            public void onChanged(Map<String, Wards> wardsMap) {
                addressViewModel.ConvertToWards(wardsMap);
            }
        });
        addressViewModel.getWards().observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                if (strings != null) {
                    wards = strings;
                    setupACTwards();
                } else {
                    wards= new String[0];
                }
            }
        });


        addressViewModel.tp.observe(this, s -> {
            if (s != null) {
                addressViewModel.loadDistricts();
                addressViewModel.quan.setValue("");
            }
        });

        addressViewModel.quan.observe(this, s -> {
            if (s != null) {
                addressViewModel.loadWards();
                addressViewModel.phuong.setValue("");
            }
        });

        addressViewModel.ht.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!addressViewModel.isValidName()){
                    mActivityAddressBinding.addressHT.setError("Họ tên phải từ 6-50 ký tự và không có ký tự đặc biệt");
                }
                else mActivityAddressBinding.addressHT.setErrorEnabled(false);
            }
        });

        addressViewModel.sdt.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!addressViewModel.isValidPhone()){
                    mActivityAddressBinding.addressSDT.setError("Số điện thoại phải là 10 hoặc 11 số");
                }
                else mActivityAddressBinding.addressSDT.setErrorEnabled(false);
            }
        });

        addressViewModel.duong.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!addressViewModel.isValidAddress()){
                    mActivityAddressBinding.addressDC.setError("Địa chỉ phải có ít nhất số nhà, tên đường");
                }
                else mActivityAddressBinding.addressDC.setErrorEnabled(false);
            }
        });

        addressViewModel.quan.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!addressViewModel.isValidCity()){
                    mActivityAddressBinding.addressQuan.setError("Bạn chưa nhập tỉnh / thành phố");
                }
                else mActivityAddressBinding.addressQuan.setErrorEnabled(false);
            }
        });

        addressViewModel.phuong.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!addressViewModel.isValidDistrict()){
                    mActivityAddressBinding.addressPhuong.setError("Bạn chưa nhập quận / huyên");
                }
                else mActivityAddressBinding.addressPhuong.setErrorEnabled(false);
            }
        });

        addressViewModel.getContact().observe(this, new Observer<Contact>() {
            @Override
            public void onChanged(Contact contact) {
                Log.d("AddressViewModel", contact.getId());
                Log.d("AddressViewModel", contact.getTelephone());
                Log.d("AddressViewModel", contact.getCity());
                Log.d("AddressViewModel", contact.getDistrict());
                Log.d("AddressViewModel", contact.getWard());
                Log.d("AddressViewModel", contact.getLine());
            }
        });

    }


    private void setupACTcities() {

        AutoCompleteTextView ACTcities = mActivityAddressBinding.city;
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cities);
        ACTcities.setAdapter(arrayAdapter1);

    }

    private void setupACTdistricts() {

        AutoCompleteTextView ACTdistricts = mActivityAddressBinding.district;
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, districts);
        ACTdistricts.setAdapter(arrayAdapter2);
    }

    private void setupACTwards() {

        AutoCompleteTextView ACTwards = mActivityAddressBinding.wards;
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, wards);
        ACTwards.setAdapter(arrayAdapter3);
    }
}
