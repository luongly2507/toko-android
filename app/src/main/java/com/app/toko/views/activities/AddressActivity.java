package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
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
import com.app.toko.models.Contact;
import com.app.toko.viewmodels.AddressViewModel;

public class AddressActivity extends AppCompatActivity {

    private ActivityAddressBinding mActivityAddressBinding;
    private View mView;
    private AddressViewModel addressViewModel;
    private String[] cities;
    private String[] districts;
    private String[] wards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityAddressBinding = ActivityAddressBinding.inflate(getLayoutInflater());
        mView = mActivityAddressBinding.getRoot();
        addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);

        mActivityAddressBinding.setLifecycleOwner(this);
        mActivityAddressBinding.setAddressViewModel(addressViewModel);
        setContentView(mView);

        mActivityAddressBinding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddressActivity.this, "Back", Toast.LENGTH_SHORT).show();
            }
        });

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

                    Toast.makeText(AddressActivity.this, "Xác Nhận", Toast.LENGTH_SHORT).show();
                    addressViewModel.registerAddress();

                } else {
                    Toast.makeText(AddressActivity.this, "Không thể xác nhận", Toast.LENGTH_SHORT).show();
                }
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

        // Khi khởi tạo Activity, cần gọi phương thức loadCities() trong ViewModel để tải dữ liệu thành phố
        addressViewModel.loadCities();


        // Quận / Huyện
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

        addressViewModel.getContact().observe(this, new Observer<Contact>() {
            @Override
            public void onChanged(Contact contact) {
                Log.d("AddressViewModel", contact.getTelephone());
                Log.d("AddressViewModel", contact.getCity());
                Log.d("AddressViewModel", contact.getDistrict());
                Log.d("AddressViewModel", contact.getWard());
                Log.d("AddressViewModel", contact.getLine());
            }
        });


        addressViewModel.tp.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null)
                {
                    addressViewModel.quan.setValue("");
                    addressViewModel.phuong.setValue("");
                    addressViewModel.loadCities();
                }
            }
        });

        addressViewModel.quan.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s != null){
                    addressViewModel.phuong.setValue("");
                    addressViewModel.loadDistricts();
                    addressViewModel.loadWards();
                }
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
