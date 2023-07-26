package com.app.toko.services;

import com.app.toko.models.Contact;
import com.app.toko.models.Order;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderService {
    @GET("api/v1/users/{id}/orders/")
    Call<List<Order>> getAllOrders(@Path("id") UUID id,
                                    @Header("Authorization") String authHeader);
    @POST("api/v1/users/{id}/orders")
    Call<Order> createNewOrder(@Body Order order,
                                   @Path("id") UUID id,
                                   @Header("Authorization") String authHeader );

}
