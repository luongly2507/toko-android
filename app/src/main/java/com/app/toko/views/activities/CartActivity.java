package com.app.toko.views.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.adapters.CartItemAdapter;
import com.app.toko.databinding.ActivityCartBinding;
import com.app.toko.repositories.UserRepository;
import com.app.toko.viewmodels.CartViewModel;

import java.util.UUID;


public class CartActivity extends AppCompatActivity {
    private CartViewModel cartViewModel;
    private ActivityCartBinding binding;
    private RecyclerView recyclerView;
    private CartItemAdapter cartItemAdapter;
    private String userIdString;
    private String token;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        binding.setCartViewModel(cartViewModel);

        setContentView(binding.getRoot());

        // Set up button back
        binding.buttonBack.setOnClickListener(v -> {
            onBackPressed();
        });

        // Set up RecyclerView
        recyclerView = binding.recyclerViewCart;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItemAdapter = new CartItemAdapter();
        recyclerView.setAdapter(cartItemAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userIdString = extras.getString("user_id");
            token = extras.getString("token");
        }
        else {
            Toast.makeText(this, "Không nhận được intent token", Toast.LENGTH_SHORT).show();
        }

        if (userIdString != null && token != null) {
            cartViewModel.fetchCartItems(UUID.fromString(userIdString), token);
        }
        else {
            Toast.makeText(this, "Không nhận được token", Toast.LENGTH_SHORT).show();
        }

        cartViewModel.getCartItemsLiveData().observe(this, cartItems -> cartItemAdapter.setCartItemList(cartItems));
    }
}
