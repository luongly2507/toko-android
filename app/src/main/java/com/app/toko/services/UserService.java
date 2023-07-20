package com.app.toko.services;

import com.app.toko.payload.request.UpdateCartItemRequest;
import com.app.toko.payload.response.CartResponse;
import com.app.toko.models.User;
import com.app.toko.payload.response.BookResponse;
import com.app.toko.payload.response.CartResponse;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("api/v1/users/{id}")
    Call<User> getUserDetail(@Path("id") UUID id, @Header("Authorization") String authHeader);

    @POST("api/v1/auth/register")
    Call<Void> register(@Body User user);

    @PUT("/api/v1/users/phone/{phone}")
    Call<Void> updateUserPassword(@Path("phone") String phone , @Query("password") String password);

    @GET("api/v1/users")
    Call<Void> isExistUserByPhone(@Query("phone") String phone);

    @GET("api/v1/users/{id}/carts/")
    Call<List<CartResponse>> getUserCartItems(@Path("id") UUID userId, @Header("Authorization") String authHeader);

    @PUT("api/v1/users/{id}/carts/")
    Call<Void> updateCartItem(@Path("id") UUID id , @Header("Authorization") String authHeader , @Body UpdateCartItemRequest updateCartItemRequest);
}
