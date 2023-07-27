package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.toko.R;
import com.app.toko.adapters.CategoryRecyclerViewAdapter;
import com.app.toko.adapters.OrderHistoryRecyclerViewAdapter;
import com.app.toko.databinding.ActivityLoginBinding;
import com.app.toko.databinding.ActivityOrderHistoryBinding;
import com.app.toko.models.Category;
import com.app.toko.models.Order;
import com.app.toko.viewmodels.LoginViewModel;
import com.app.toko.viewmodels.OrderHistoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderHistoryActivity extends AppCompatActivity {
    OrderHistoryViewModel orderHistoryViewModel;
    ActivityOrderHistoryBinding binding;
    SharedPreferences sharedPreferences;
    String strUserId, accessToken;
    RecyclerView myOrderRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());

        orderHistoryViewModel = new ViewModelProvider(this).get(OrderHistoryViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setOrderHistoryViewModel(orderHistoryViewModel);
        setContentView(binding.getRoot());

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences("toko-preferences" , MODE_PRIVATE);
        strUserId = sharedPreferences.getString("user_id" , null);
        accessToken = sharedPreferences.getString("access_token" , null);
        orderHistoryViewModel.getAllOrders(UUID.fromString(strUserId), "Bearer " + accessToken);
        orderHistoryViewModel.getOrdersLiveData().observe(this, new Observer<List<Order>>() {
                    @Override
                    public void onChanged(List<Order> orders) {
                        myOrderRecyclerView = binding.myOrdersRecyclerView;
                        myOrderRecyclerView.setAdapter(new OrderHistoryRecyclerViewAdapter(orders, getApplicationContext()));
                        myOrderRecyclerView.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));
                    }
                });

    }
}