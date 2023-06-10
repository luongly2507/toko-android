package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.app.toko.databinding.ActivitySearchResultBinding;

public class SearchResultActivity extends AppCompatActivity {

    private ActivitySearchResultBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonBack.setOnClickListener(v -> {
            onBackPressed();
        });
        String categoryName = getIntent().getStringExtra("category");
        if (categoryName != null){
            binding.editTextSearch.setText(categoryName);
        }

    }

}