package com.app.toko.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.app.toko.models.Category;
import com.app.toko.repositories.CategoryRepository;

import java.util.List;

public class DashboardViewModel extends ViewModel {

    private CategoryRepository categoryRepository;
    private LiveData<List<Category>> categoriesLiveData;


    public DashboardViewModel() {
        categoryRepository = new CategoryRepository();
        categoriesLiveData = categoryRepository.getCategoriesLiveData();
    }

    public void getAllCategories() {
        categoryRepository.getAllCategories();
    }

    public LiveData<List<Category>> getCategoryResponsesLiveData() {
        return categoriesLiveData;
    }
}