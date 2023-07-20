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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
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
    private boolean isConnectedToServer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        binding.setCartViewModel(cartViewModel);

        setContentView(binding.getRoot());

        // Thiết lập sự kiện cho nút back
        binding.buttonBack.setOnClickListener(v -> {
            onBackPressed();
        });

        // Thiết lập sự kiện cho checkbox Tất cả
        binding.checkBoxSelectAll.setOnClickListener(view -> {
            boolean isChecked = binding.checkBoxSelectAll.isChecked();
            cartItemAdapter.checkAllItems(isChecked);
        });

        // Khởi tạo RecyclerView và adapter
        recyclerView = binding.recyclerViewCart;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItemAdapter = new CartItemAdapter();
        recyclerView.setAdapter(cartItemAdapter);

        // Lấy userId và token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("toko-preferences", Context.MODE_PRIVATE);
        userIdString = sharedPreferences.getString("user_id", null);
        token = sharedPreferences.getString("access_token", null);

        // Lấy danh sách giỏ hàng từ server
        cartViewModel.getCartResponsesLiveData().observe(this, cartResponses -> {
            if (cartResponses != null) {
                List<CartItem> cartItems = cartResponses.stream()
                        .map(CartItem::fromCartResponse)
                        .collect(Collectors.toList());
                cartItemAdapter.setCartItemList(cartItems);

                // Hiển thị tổng giá tiền
                BigDecimal totalPrice = cartItemAdapter.calculateTotalPrice();
                binding.textViewTotalPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi" , "VN")).format(totalPrice));
            } else {
                Toast.makeText(this, "Không nhận được danh sách giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });

        if (userIdString != null && token != null) {
            cartViewModel.fetchCartItems(UUID.fromString(userIdString), token);
            isConnectedToServer = true;
        }
        else {
            isConnectedToServer = false;
            binding.checkBoxSelectAll.setEnabled(false);
            Toast.makeText(this, "Không nhận được token", Toast.LENGTH_SHORT).show();
        }
    }
}
