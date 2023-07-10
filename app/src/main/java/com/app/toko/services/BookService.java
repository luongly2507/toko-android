package com.app.toko.services;

import com.app.toko.payload.response.BookResponse;
import com.app.toko.payload.response.Page;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookService {
    @GET("api/v1/books/search/categories/")
    Call<Page<BookResponse>> getAllBooksByCategory(@Query("categoryName") String category ,@Query("language") String language,@Query("sort") String sort, @Query("page") int pageNumber);
    @GET("api/v1/books")
    Call<Page<BookResponse>> getAllBooks();

    @GET("api/v1/books")
    Call<Page<BookResponse>> getAllBooksByPage(@Query("page") int pageNumber);

    @GET("api/v1/books/search/")
    Call<Page<BookResponse>> getAllBookByTitle(@Query("title") String title  , @Query("language") String language , @Query("sort") String sort , @Query("page") int pageNumber );

}
