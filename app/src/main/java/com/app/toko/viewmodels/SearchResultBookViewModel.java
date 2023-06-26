package com.app.toko.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.app.toko.payload.response.BookResponse;
import com.app.toko.repositories.BookRespository;

import java.util.List;

public class SearchResultBookViewModel extends ViewModel {
    private BookRespository bookRespository;
    private LiveData<List<BookResponse>> bookResponseLiveData;
    public SearchResultBookViewModel()
    {
        bookRespository = new BookRespository();
        bookResponseLiveData = bookRespository.getBookResponseLiveData();
    }
    public void getAllBooks()
    {
        bookRespository.getAllBooks();
    }

    public LiveData<List<BookResponse>> getBookResponseLiveData() {
        return bookResponseLiveData;
    }
}
