package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.toko.R;
import com.app.toko.databinding.ActivitySearchBookBinding;

public class SearchBookActivity extends AppCompatActivity {

    private ActivitySearchBookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.arrowBackIcon.setOnClickListener(v -> {
            onBackPressed();
        });
        String categoryName = getIntent().getStringExtra("category");
        if (categoryName != null){
            binding.editTextSearchBook.setText(categoryName);
        }

    }

}