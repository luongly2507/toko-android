package com.app.toko.services;


import com.app.toko.models.Category;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryService {
    @GET("api/v1/categories")
    Call<List<Category>> getAllCategories();
}