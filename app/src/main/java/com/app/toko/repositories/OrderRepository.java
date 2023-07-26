package com.app.toko.repositories;

import static com.google.gson.internal.bind.TypeAdapters.UUID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.models.Category;
import com.app.toko.models.Order;
import com.app.toko.services.OrderService;
import com.app.toko.utils.ApiService;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    OrderService orderService;
    MutableLiveData<List<Order>> ordersLiveData;

    public OrderRepository(OrderService orderService, MutableLiveData<List<Order>> ordersLiveData) {
        this.orderService = ApiService.getOrderService();
        this.ordersLiveData = ordersLiveData;
    }
    public void getAllOrders(java.util.UUID userId, String access_token) {
        orderService.getAllOrders(userId, access_token).enqueue(
                new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        System.out.println(response.body());
                        if (response.body() != null) {
                            ordersLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {
                        t.printStackTrace();
                        ordersLiveData.postValue(null);
                    }
                }
        );
    }
    public void createNewOrder(Order order, UUID userId, String access_token) {
        orderService.createNewOrder(order, userId, access_token).enqueue(
                new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {

                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {

                    }
                }
        );
    }
    public LiveData<List<Order>> getOrdersLiveData() {
        return ordersLiveData;
    }
}
