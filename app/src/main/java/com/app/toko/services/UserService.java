package com.app.toko.services;

import com.app.toko.models.CartItem;
import com.app.toko.models.Category;
import com.app.toko.models.User;

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

    //http://localhost:3000/api/v1/users/90d6d0d0-7187-4685-83b5-ffa91bd8ef0f/carts/
    @GET("api/v1/users/{id}/carts")
    Call<List<CartItem>> getUserCartItems(@Path("id") UUID userId, @Header("Authorization") String authHeader);

}
