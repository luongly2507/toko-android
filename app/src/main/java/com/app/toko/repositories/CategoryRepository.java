package com.app.toko.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.models.Category;
import com.app.toko.services.CategoryService;
import com.app.toko.utils.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private CategoryService categoryService;
    private MutableLiveData<List<Category>> categoriesLiveData;

    public CategoryRepository() {
        categoriesLiveData = new MutableLiveData<>();
        categoryService = ApiService.getCategoryService();
    }

    public void getAllCategories() {
        categoryService.getAllCategories().enqueue(
                new Callback<List<Category>>() {
                    @Override
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                        System.out.println(response.body());
                        if (response.body() != null) {
                            categoriesLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {
                        t.printStackTrace();
                        categoriesLiveData.postValue(null);
                    }
                }
        );
    }

    public LiveData<List<Category>> getCategoriesLiveData() {
        return categoriesLiveData;
    }
}
