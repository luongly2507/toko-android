package com.app.toko.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.models.Order;
import com.app.toko.models.OrderDetail;
import com.app.toko.services.OrderDetailService;
import com.app.toko.services.OrderService;
import com.app.toko.utils.ApiService;

import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailRepository {
    OrderDetailService orderDetailService;
    MutableLiveData<List<OrderDetail>> orderDetailsLiveData;

    public OrderDetailRepository(OrderDetailService orderDetailService, MutableLiveData<List<OrderDetail>> orderDetailsLiveData) {
        this.orderDetailService = ApiService.getOrderDetailService();
        this.orderDetailsLiveData = orderDetailsLiveData;
    }
    public void getAllOrderDetails(java.util.UUID userId, String access_token) {
        orderDetailService.getAllOrderDetails(userId, access_token).enqueue(
                new Callback<List<OrderDetail>>() {
                    @Override
                    public void onResponse(Call<List<OrderDetail>> call, Response<List<OrderDetail>> response) {
                        System.out.println(response.body());
                        if (response.body() != null) {
                            orderDetailsLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<OrderDetail>> call, Throwable t) {
                        t.printStackTrace();
                        orderDetailsLiveData.postValue(null);
                    }
                }
        );
    }
    public void createNewOrderDetail(OrderDetail orderDetail, UUID userId, String access_token) {
        orderDetailService.createNewOrderDetails(orderDetail, userId, access_token).enqueue(
                new Callback<OrderDetail>() {
                    @Override
                    public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {

                    }

                    @Override
                    public void onFailure(Call<OrderDetail> call, Throwable t) {

                    }
                }
        );
    }
    public LiveData<List<OrderDetail>> getOrderDetailsLiveData() {
        return orderDetailsLiveData;
    }
}
