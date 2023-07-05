package com.app.toko.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.toko.payload.response.BookResponse;
import com.app.toko.repositories.BookRespository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private BookRespository bookRespository;
    private LiveData<List<BookResponse>> bookResponseLivaData;

    public HomeViewModel() {
        bookRespository = new BookRespository();
        bookResponseLivaData = bookRespository.getBookResponseLiveData();
    }
    public boolean getMoreBook(int pageNumber)
    {
        if(pageNumber + 1 < bookRespository.getTotalPages())
        {
            this.getAllBooksByPage(pageNumber);
            return true;
        }
        return false;
    }
    public LiveData<List<BookResponse>> getBookResponseLivaData() {
        return bookResponseLivaData;
    }

    public void getAllBooks()
    {
        bookRespository.getAllBooks();
    }
    public void getAllBooksByPage(int page) {bookRespository.getAllBooksByPage(page);}
}