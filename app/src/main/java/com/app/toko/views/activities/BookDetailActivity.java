package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.toko.R;
import com.app.toko.adapters.ViewPagerAdapter;
import com.app.toko.adapters.ViewPagerBookImageAdapter;
import com.app.toko.databinding.ActivityBookDetailBinding;
import com.app.toko.databinding.ActivitySearchResultBinding;
import com.app.toko.payload.response.BookResponse;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

public class BookDetailActivity extends AppCompatActivity {
    private ActivityBookDetailBinding binding;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        BookResponse selectedBook = (BookResponse) getIntent().getSerializableExtra("BookDetail");
        if(selectedBook != null)
        {
            ViewPagerBookImageAdapter adapter = new ViewPagerBookImageAdapter(selectedBook.getAlbums().stream().map(album -> album.getImageSource()).collect(Collectors.toList()));
            binding.viewPager2ImageBook.setAdapter(adapter);
            binding.textViewTitle.setText(selectedBook.getTitle());
            binding.textViewBookId.setText(selectedBook.getId().toString());
            String price = DecimalFormat.getCurrencyInstance(new Locale("vi" , "VN")).format(selectedBook.getPrice());
            binding.textViewPrice.setText(price);
            binding.textViewAuthor.setText(selectedBook.getAuthors());
            binding.textViewPublisher.setText(selectedBook.getPublisher());
            binding.textViewYear.setText(selectedBook.getPublishcationDate());
            binding.textViewDescription.setText(selectedBook.getDescription());
            binding.textViewDetailBook.setText(selectedBook.getTitle());
        }
    }
}