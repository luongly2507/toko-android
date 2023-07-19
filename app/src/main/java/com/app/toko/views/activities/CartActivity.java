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
        sharedPreferences = getSharedPreferences("toko-preferences" , MODE_PRIVATE);
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


        userIdString = sharedPreferences.getString("user_id" , null);
        token = sharedPreferences.getString("access_token" , null);

        if (userIdString != null && token != null) {
            cartViewModel.fetchCartItems(UUID.fromString(userIdString), token);
        }
        else {
            Toast.makeText(this, "Lỗi xảy ra!", Toast.LENGTH_SHORT).show();
        }

        cartViewModel.getCartItemsLiveData().observe(this, cartItems -> cartItemAdapter.setCartItemList(cartItems));
    }
}
