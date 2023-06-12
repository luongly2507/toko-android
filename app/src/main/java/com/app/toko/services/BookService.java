package com.app.toko.services;

import com.app.toko.payload.response.BookResponse;
import com.app.toko.payload.response.PageBookResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookService {
    @GET("api/v1/books/search/categories/")
    Call<PageBookResponse> getAllBooksByCategory(@Query("categoryName") String category , @Query("page") int pageNumber);
}
