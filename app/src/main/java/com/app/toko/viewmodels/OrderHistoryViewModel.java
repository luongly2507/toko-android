package com.app.toko.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.app.toko.models.Category;
import com.app.toko.models.Order;
import com.app.toko.repositories.CategoryRepository;
import com.app.toko.repositories.OrderRepository;

import java.util.List;
import java.util.UUID;

public class OrderHistoryViewModel extends ViewModel {
    private OrderRepository orderRepository;
    private LiveData<List<Order>> ordersLiveData;

    public OrderHistoryViewModel() {
        this.orderRepository = new OrderRepository();
        this.ordersLiveData = orderRepository.getOrdersLiveData();
    }

    public void getAllOrders(UUID userId, String access_token) {
        orderRepository.getAllOrders(userId, access_token);
    }

    public LiveData<List<Order>> getOrdersLiveData() {
        return ordersLiveData;
    }
}
