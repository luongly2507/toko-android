package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.app.toko.R;
import com.app.toko.adapters.OrderHistoryDetailProductRecyclerViewAdapter;
import com.app.toko.adapters.OrderProductRecyclerViewAdapter;
import com.app.toko.databinding.ActivityOrderHistoryBinding;
import com.app.toko.databinding.ActivityOrderHistoryDetailBinding;
import com.app.toko.models.Contact;
import com.app.toko.models.Order;
import com.app.toko.models.OrderDetail;
import com.app.toko.payload.request.CreateOrderDetailRequest;
import com.app.toko.viewmodels.OrderHistoryViewModel;
import com.app.toko.viewmodels.OrderHitstoryDetailViewModel;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderHistoryDetailActivity extends AppCompatActivity {

    OrderHitstoryDetailViewModel orderHitstoryDetailViewModel;
    ActivityOrderHistoryDetailBinding binding;
    SharedPreferences sharedPreferences;
    RecyclerView myOrderRecyclerView;
    Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryDetailBinding.inflate(getLayoutInflater());

        orderHitstoryDetailViewModel = new ViewModelProvider(this).get(OrderHitstoryDetailViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setOrderHistoryDetailViewModel(orderHitstoryDetailViewModel);
        setContentView(binding.getRoot());

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences("toko-preferences", Context.MODE_PRIVATE);
        String userIdString = sharedPreferences.getString("user_id", null);
        String token = sharedPreferences.getString("access_token", null);

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order_data");

        binding.deliverDate.setText(order.getPurchaseDate());
        BigDecimal totalValue = BigDecimal.valueOf(0);
        for (OrderDetail orderDetail:
             order.getOrderDetails()) {
            totalValue = totalValue.add(orderDetail.getPrice().multiply(orderDetail.getQuantity()));
        }
        binding.productValue.setText(totalValue.toString());
        double totalCost = totalValue.doubleValue() + 14000;
        binding.totalCostInfo.setText(String.valueOf(totalCost));
        Contact contact = order.getContact();
        binding.userNameTextView.setText(contact.getReceiver());
        binding.userPhoneNumberTextView.setText(contact.getTelephone());
        binding.addressTextView.setText(contact.getLine()
                + ", " + contact.getWard()
                + ", " + contact.getDistrict()
                + ", " + contact.getCity());
        OrderHistoryDetailProductRecyclerViewAdapter adapter = new OrderHistoryDetailProductRecyclerViewAdapter(order.getOrderDetails());
        binding.orderedProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.orderedProductsRecyclerView.setAdapter(adapter);
    }
}