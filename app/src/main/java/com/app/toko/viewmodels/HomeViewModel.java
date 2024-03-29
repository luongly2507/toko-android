package com.app.toko.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.toko.payload.response.BookResponse;
import com.app.toko.repositories.BookRespository;

import java.util.List;

public class    HomeViewModel extends ViewModel {

    private BookRespository bookRespository;
    private LiveData<List<BookResponse>> bookResponseLivaData;
    public HomeViewModel() {
        bookRespository = new BookRespository();
        bookResponseLivaData = bookRespository.getBookResponseLiveData();

    }
    public void getAllBooksByPurchase(String sort)
    {
        bookRespository.getAllBooksByPurchase(sort);
    }
    public void getAllBooks()
    {
        bookRespository.getAllBooks();
    }
    public void getAllBooksBySort(String sort , String language)
    {
        bookRespository.getAllBooksByTitle("" , language , sort , 0);
    }

    public LiveData<List<BookResponse>> getBookResponseLivaData() {
        return bookResponseLivaData;
    }


}