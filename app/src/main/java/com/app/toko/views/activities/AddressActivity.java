package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.app.toko.R;
import com.app.toko.databinding.ActivityAddressBinding;
import com.app.toko.viewmodels.AddressViewModel;

public class AddressActivity extends AppCompatActivity {

    private ActivityAddressBinding mActivityAddressBinding;
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityAddressBinding = ActivityAddressBinding.inflate(getLayoutInflater());
        mView = mActivityAddressBinding.getRoot();

        AddressViewModel addressViewModel = new AddressViewModel("","","","","","","");
        mActivityAddressBinding.setAddressViewModel(addressViewModel);

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
                        && addressViewModel.isValidDítrict()
                        && addressViewModel.isValidWards()
                        && addressViewModel.isValidAddress()
                        && addressViewModel.isValidLocation())
                {
                    Toast.makeText(AddressActivity.this, "Xác Nhận", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(AddressActivity.this, "Không thể xác nhận", Toast.LENGTH_SHORT).show();
            }
        });

        setContentView(mView);
        setupAutoCompleteTextViews();


    }

    private void setupAutoCompleteTextViews() {
        AutoCompleteTextView ACTcities = mActivityAddressBinding.city;
        String[] cities = getResources().getStringArray(R.array.city);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cities);
        ACTcities.setAdapter(arrayAdapter1);

        AutoCompleteTextView ACTdistricts = mActivityAddressBinding.district;
        String[] districts = getResources().getStringArray(R.array.district);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, districts);
        ACTdistricts.setAdapter(arrayAdapter2);

        AutoCompleteTextView ACTwards = mActivityAddressBinding.wards;
        String[] wards = getResources().getStringArray(R.array.wards);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, wards);
        ACTwards.setAdapter(arrayAdapter3);
    }
}