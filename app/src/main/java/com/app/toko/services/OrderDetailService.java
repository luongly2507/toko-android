package com.app.toko.services;

import com.app.toko.models.Order;
import com.app.toko.models.OrderDetail;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderDetailService {
    @GET("api/v1/users/{id}/order_details/")
    Call<List<OrderDetail>> getAllOrderDetails(@Path("id") UUID id,
                                   @Header("Authorization") String authHeader);
    @POST("api/v1/users/{id}/orders")
    Call<OrderDetail> createNewOrderDetails(@Body OrderDetail orderDetail,
                               @Path("id") UUID id,
                               @Header("Authorization") String authHeader );
}
