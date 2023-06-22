package com.app.toko.repositories;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.payload.response.BookResponse;
import com.app.toko.payload.response.PageBookResponse;
import com.app.toko.services.BookService;
import com.app.toko.utils.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRespository {
    private BookService bookService;
    private MutableLiveData<List<BookResponse>> bookResponseLiveData;

    private int totalPages = 0;

    public BookRespository()
    {
        bookService = ApiService.getBookService();
        bookResponseLiveData = new MutableLiveData<>();
    }

    public void getAllBooksByCategory(String category , int pageNumber) {
        bookService.getAllBooksByCategory(category , pageNumber).enqueue(new Callback<PageBookResponse>() {
            @Override
            public void onResponse(Call<PageBookResponse> call, Response<PageBookResponse> response) {
                if (response.body() != null) {
                    bookResponseLiveData.postValue(response.body().getContent());
                    totalPages = response.body().getTotalPages();
                }
            }

            @Override
            public void onFailure(Call<PageBookResponse> call, Throwable t) {
                System.out.println("Fail: " + t);
                bookResponseLiveData.postValue(null);
                totalPages = 0;
            }
        });
    }
    public void getAllBooks()
    {
        bookService.getAllBooks().enqueue(new Callback<PageBookResponse>() {
            @Override
            public void onResponse(Call<PageBookResponse> call, Response<PageBookResponse> response) {
                if (response.body() != null) {
                    bookResponseLiveData.postValue(response.body().getContent());
                    totalPages = response.body().getTotalPages();
                }
            }

            @Override
            public void onFailure(Call<PageBookResponse> call, Throwable t) {
                System.out.println("Fail: " + t);
                bookResponseLiveData.postValue(null);
                totalPages = 0;
            }
        });
    }
    public LiveData<List<BookResponse>> getBookResponseLiveData()
    {
        return this.bookResponseLiveData;
    }
    public int getTotalPages()
    {
        return totalPages;
    }
}
