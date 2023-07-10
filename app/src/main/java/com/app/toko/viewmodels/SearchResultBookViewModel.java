package com.app.toko.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.app.toko.payload.response.BookResponse;
import com.app.toko.repositories.BookRespository;

import java.util.List;

public class SearchResultBookViewModel extends ViewModel {
    private BookRespository bookRespository;
    private LiveData<List<BookResponse>> bookResponseLiveData;
    public LiveData<Integer> totalPages;


    public SearchResultBookViewModel()
    {
        bookRespository = new BookRespository();
        bookResponseLiveData = bookRespository.getBookResponseLiveData();
        totalPages = bookRespository.getTotalPagesLiveData();
    }
    /*public void getAllBooks()
    {
        bookRespository.getAllBooks();
    }*/
    public void getAllBookByTitle(String title ,String language, String sort , int pageNumber)
    {
        bookRespository.getAllBooksByTitle(title ,language, sort , pageNumber);

    }
    public LiveData<List<BookResponse>> getBookResponseLiveData() {
        return bookResponseLiveData;
    }
}
