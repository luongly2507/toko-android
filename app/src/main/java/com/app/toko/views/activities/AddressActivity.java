package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.app.toko.R;

public class AddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        AutoCompleteTextView ACTcities = findViewById(R.id.city);
        String[] cities = getResources().getStringArray(R.array.city);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cities);
        ACTcities.setAdapter(arrayAdapter1);

        AutoCompleteTextView ACTdistricts = findViewById(R.id.district);
        String[] districts = getResources().getStringArray(R.array.district);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, districts);
        ACTdistricts.setAdapter(arrayAdapter2);

        AutoCompleteTextView ACTwards = findViewById(R.id.wards);
        String[] wards = getResources().getStringArray(R.array.wards);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, wards);
        ACTwards.setAdapter(arrayAdapter3);
    }
}