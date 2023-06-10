package com.app.toko.services;

import com.app.toko.models.Category;
import com.app.toko.models.User;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface UserService {
    @GET("api/v1/users/{id}")
    Call<User> getUserDetail(@Path("id") UUID id, @Header("Authorization") String authHeader);
}
