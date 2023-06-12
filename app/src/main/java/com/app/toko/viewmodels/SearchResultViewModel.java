package com.app.toko.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.payload.response.BookResponse;
import com.app.toko.repositories.BookRespository;

import java.util.List;

public class SearchResultViewModel extends AndroidViewModel {
    private LiveData<List<BookResponse>> bookResponseLiveData;
    private BookRespository bookRespository;

    public SearchResultViewModel(Application application)
    {
        super(application);
        bookRespository = new BookRespository();
        bookResponseLiveData = bookRespository.getBookResponseLiveData();
    }
    public void getAllBooksByCategory(String category , int pageNumber)
    {
        bookRespository.getAllBooksByCategory(category , pageNumber);
    }
    public boolean getMoreBook(String category , int pageNumber)
    {
        if(pageNumber + 1 < bookRespository.getTotalPages())
        {
            this.getAllBooksByCategory(category , pageNumber + 1);
            return true;
        }
        return false;
    }
    public LiveData<List<BookResponse>> getBookResponseLiveData()
    {
        return bookResponseLiveData;
    }
}
