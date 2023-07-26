package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.toko.R;
import com.app.toko.adapters.ViewPagerAdapter;
import com.app.toko.adapters.ViewPagerBookImageAdapter;
import com.app.toko.databinding.ActivityBookDetailBinding;
import com.app.toko.databinding.ActivitySearchResultBinding;
import com.app.toko.payload.request.UpdateCartItemRequest;
import com.app.toko.payload.response.BookResponse;
import com.app.toko.viewmodels.BookDetailViewModel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookDetailActivity extends AppCompatActivity {
    private ActivityBookDetailBinding binding;
    private BookDetailViewModel bookDetailViewModel;
    private SharedPreferences sharedPreferences;
    private String strUserId , accessToken;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookDetailBinding.inflate(getLayoutInflater());
        bookDetailViewModel = new ViewModelProvider(this).get(BookDetailViewModel.class);
        sharedPreferences = getSharedPreferences("toko-preferences" , MODE_PRIVATE);
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        strUserId = sharedPreferences.getString("user_id" , null);
        accessToken = sharedPreferences.getString("access_token" , null);
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

        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(BookDetailActivity.this , MainActivity.class);
                String value = null;
                switch (item.getItemId())
                {
                    case R.id.item_home:
                        value = "home";
                        break;
                    case R.id.item_category:
                        value = "category";
                        break;
                    case R.id.item_account:
                        value = "account";
                        break;
                    case R.id.item_cart:
                        Intent intentCart = new Intent(BookDetailActivity.this , CartActivity.class);
                        startActivity(intentCart);
                        break;
                    case R.id.item_search:
                        Intent intentSearch = new Intent(BookDetailActivity.this , SearchBookActivity.class);
                        startActivity(intentSearch);
                        break;
                    default:
                        break;
                }
                if(value != null && !value.isBlank())
                {
                    intent.putExtra("toFrag" , value );
                    startActivity(intent);
                }
                return true;
            }
        });
        binding.imageButtonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = 0;
                try {
                    amount = Integer.parseInt(binding.textViewAmount.getText().toString());
                }
                catch (Exception ex)
                {
                    Log.e("Parse Fail","Parse integer fail!!");
                }
                if(amount - 1 > 0)
                {
                    amount--;
                    binding.textViewAmount.setText(String.valueOf(amount));
                }

            }
        });
        binding.imageButtonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = 0;
                try {
                    amount = Integer.parseInt(binding.textViewAmount.getText().toString());
                }
                catch (Exception ex)
                {
                    Log.e("Parse Fail","Parse integer fail!!");
                }
                if(amount > 0)
                {
                    amount++;
                    binding.textViewAmount.setText(String.valueOf(amount));
                }
            }
        });
        
        bookDetailViewModel.getIsSuccessful().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccessful) {
                if (isSuccessful)
                {
                    Intent intent = new Intent(BookDetailActivity.this , CartActivity.class);
                    startActivity(intent);
                }
                else Toast.makeText(BookDetailActivity.this, "Lỗi zzz", Toast.LENGTH_SHORT).show();
            }
        });
        binding.buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(accessToken == null || accessToken.isBlank())
                {
                    Intent intent = new Intent(BookDetailActivity.this , LoginActivity.class);
                    startActivity(intent);
                }
                else
                {
                    if(selectedBook != null)
                    {
                        UpdateCartItemRequest updateCartItemRequest = new UpdateCartItemRequest(selectedBook.getId() , Integer.parseInt(binding.textViewAmount.getText().toString()));
                        bookDetailViewModel.updateCartItem(UUID.fromString(strUserId) , accessToken , updateCartItemRequest);
                    }
                }
            }
        });
    }


}