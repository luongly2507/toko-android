package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.toko.R;
import com.app.toko.adapters.OrderProductRecyclerViewAdapter;
import com.app.toko.databinding.ActivityConfirmOrderBinding;
import com.app.toko.models.CartItem;
import com.app.toko.models.Contact;
import com.app.toko.payload.request.CreateOrderDetailRequest;
import com.app.toko.payload.request.CreateOrderRequest;
import com.app.toko.viewmodels.ConfirmOrderViewModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ConfirmOrderActivity extends AppCompatActivity {

    ActivityConfirmOrderBinding binding;
    OrderProductRecyclerViewAdapter adapter;
    ConfirmOrderViewModel confirmOrderViewModel;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences("toko-preferences", Context.MODE_PRIVATE);
        String userIdString = sharedPreferences.getString("user_id", null);
        String token = sharedPreferences.getString("access_token", null);
        String contactIdString = getIntent().getStringExtra("contact_id");
        confirmOrderViewModel = new ViewModelProvider(this).get(ConfirmOrderViewModel.class);

        List<CartItem> selectedItemsList = (List<CartItem>) getIntent().getExtras().getSerializable("selectedItems");
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if(selectedItemsList != null)
        {
            confirmOrderViewModel.getIsSuccess().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if(aBoolean)
                    {
                        confirmOrderViewModel.deleteUserBooks(UUID.fromString(userIdString) , selectedItemsList , token);
                        Intent intent = new Intent(ConfirmOrderActivity.this , OrderSuccessActivity.class);
                        intent.putExtra("money" , binding.totalCost.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                }
            });
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime deliverDate = now.plusDays(7);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            formatter.withLocale(new Locale("vi", "VN"));
            String formattedDate = deliverDate.format(formatter);
            binding.deliverDate.setText(formattedDate);
            List<CreateOrderDetailRequest> detailRequests = new ArrayList<>();
            BigDecimal totalValue = BigDecimal.valueOf(0);
            for(CartItem cartItem : selectedItemsList)
            {
                totalValue =  totalValue.add(cartItem.getTotalPrice());
                detailRequests.add(new CreateOrderDetailRequest(cartItem.getCartQuantity() ,  cartItem.getPrice() ,UUID.fromString(cartItem.getBookId()) ));
            }
            String totalValueString = DecimalFormat.getCurrencyInstance(new Locale("vi" , "VN")).format(totalValue);
            binding.productValue.setText(totalValueString);
            totalValue = totalValue.add(new BigDecimal(14000));
            String totalCostString = DecimalFormat.getCurrencyInstance(new Locale("vi" , "VN")).format(totalValue);
            binding.totalCost.setText(totalCostString);
            binding.totalCostInfo.setText(totalCostString);
            confirmOrderViewModel.getContactById(UUID.fromString(userIdString) , UUID.fromString(contactIdString) , token);
            confirmOrderViewModel.getContactLiveData().observe(this, new Observer<Contact>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onChanged(Contact contact) {
                    if(contact != null)
                    {
                        binding.userNameTextView.setText(contact.getReceiver());
                        binding.userPhoneNumberTextView.setText(contact.getTelephone());
                        binding.addressTextView.setText(contact.getLine()
                                + ", " + contact.getWard()
                                + ", " + contact.getDistrict()
                                + ", " + contact.getCity());
                    }
                }
            });

            adapter = new OrderProductRecyclerViewAdapter(selectedItemsList);
            binding.orderedProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.orderedProductsRecyclerView.setAdapter(adapter);

            binding.orderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LocalDateTime localDateTime = LocalDateTime.now();
                    CreateOrderRequest createOrderRequest = new CreateOrderRequest(localDateTime.toString() , UUID.fromString(contactIdString) , detailRequests);
                    confirmOrderViewModel.createOrder(UUID.fromString(userIdString) , createOrderRequest , token);

                }
            });
        }
    }
}