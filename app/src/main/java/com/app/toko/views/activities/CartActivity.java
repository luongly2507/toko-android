package com.app.toko.views.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.adapters.CartItemAdapter;
import com.app.toko.databinding.ActivityCartBinding;
import com.app.toko.models.CartItem;
import com.app.toko.repositories.UserRepository;
import com.app.toko.viewmodels.CartViewModel;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


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

        // Lấy userId và token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("toko-preferences", Context.MODE_PRIVATE);
        userIdString = sharedPreferences.getString("user_id", null);
        token = sharedPreferences.getString("access_token", null);

        cartViewModel.getCartResponsesLiveData().observe(this, cartResponses -> {
            if (cartResponses != null) {
                List<CartItem> cartItems = cartResponses.stream()
                        .map(CartItem::fromCartResponse)
                        .collect(Collectors.toList());
                cartItemAdapter.setCartItemList(cartItems);
            } else {
                // Xử lý khi danh sách rỗng hoặc có lỗi
                Toast.makeText(this, "Không nhận được danh sách giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });

        if (userIdString != null && token != null) {
            cartViewModel.fetchCartItems(UUID.fromString(userIdString), token);
        }
        else {
            Toast.makeText(this, "Không nhận được token", Toast.LENGTH_SHORT).show();
        }
    }
}
