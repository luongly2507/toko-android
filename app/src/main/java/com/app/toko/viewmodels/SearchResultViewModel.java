package com.app.toko.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.models.Category;
import com.app.toko.payload.response.BookResponse;
import com.app.toko.repositories.BookRespository;
import com.app.toko.repositories.CategoryRepository;

import java.util.List;
import java.util.UUID;

public class SearchResultViewModel extends AndroidViewModel {
    private LiveData<List<BookResponse>> bookResponseLiveData;
    private BookRespository bookRespository;
    private CategoryRepository categoryRepository;

    private LiveData<List<Category>> categoryLiveData;

    public SearchResultViewModel(Application application)
    {
        super(application);
        bookRespository = new BookRespository();
        bookResponseLiveData = bookRespository.getBookResponseLiveData();
        categoryRepository = new CategoryRepository();
        categoryLiveData = categoryRepository.getCategoriesLiveData();
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
    public void getAllBooks()
    {
        bookRespository.getAllBooks();
    }
    public void getAllCategories()
    {
        categoryRepository.getAllCategories();
    }
    public LiveData<List<BookResponse>> getBookResponseLiveData()
    {
        return bookResponseLiveData;
    }
    public LiveData<List<Category>> getCategoryLiveData() {return categoryLiveData;}
}
