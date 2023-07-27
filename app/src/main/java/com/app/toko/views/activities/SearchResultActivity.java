package com.app.toko.views.activities;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.app.toko.R;
import com.app.toko.adapters.BookRecyclerViewAdapter;
import com.app.toko.databinding.ActivitySearchResultBinding;
import com.app.toko.models.Category;
import com.app.toko.payload.response.BookResponse;
import com.app.toko.viewmodels.SearchResultViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchResultActivity extends AppCompatActivity {

    private ActivitySearchResultBinding binding;
    private SearchResultViewModel searchResultViewModel;
    private BookRecyclerViewAdapter adapter;
    private List<BookResponse> bookList = new ArrayList<>();
    private List<String> categoryNameList = new ArrayList<>();
    private ArrayAdapter<String> categoryFilterAdapter;
    String language = null , sort = null;
    private String categoryName;

    private int pageNumber = 0 , oldPageNumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        searchResultViewModel = new ViewModelProvider(this).get(SearchResultViewModel.class);
        binding.buttonBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.buttonSearch.setOnClickListener(v -> {startActivity(new Intent(this, SearchBookActivity.class));});
        binding.buttonCart.setOnClickListener(v -> {startActivity(new Intent(this, CartActivity.class));});
        binding.reccyclerViewBookResult.setLayoutManager(new GridLayoutManager(this , 2));
        categoryName = getIntent().getStringExtra("category");

        if (categoryName != null){
            binding.filterCategoryList.setText(categoryName);
            searchResultViewModel.getAllBooksByCategory(categoryName ,language,sort, pageNumber);
            searchResultViewModel.getBookResponseLiveData().observe(this, new Observer<List<BookResponse>>() {
                @Override
                public void onChanged(List<BookResponse> bookResponses) {
                    //bookList.addAll(bookResponses);
                    if(pageNumber != oldPageNumber)
                    {
                        oldPageNumber = pageNumber;
                        if(bookResponses != null) bookList.addAll(bookResponses);
                    }
                    else bookList = bookResponses;
                    adapter = new BookRecyclerViewAdapter(bookList);
                    binding.reccyclerViewBookResult.setAdapter(adapter);
                }
            });
            searchResultViewModel.totalPages.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if(integer - 1 <= pageNumber)
                    {
                        binding.buttonMore.setVisibility(GONE);
                        binding.textViewNothing.setVisibility(View.VISIBLE);
                    } else binding.buttonMore.setVisibility(View.VISIBLE);
                }
            });
            searchResultViewModel.getAllCategories();
            searchResultViewModel.getCategoryLiveData().observe(this, new Observer<List<Category>>() {
                @Override
                public void onChanged(List<Category> categories) {
                    if(categories != null)
                    {
                        for(Category c : categories)
                        {
                            if(Objects.equals(c.getName(), categoryName))
                            {
                                categoryNameList.add(categoryName);
                                if (c.getChildren() != null) {
                                    categoryNameList.addAll(c.getChildren().stream().map(category -> category.getName()).collect(Collectors.toList()));
                                }
                                break;
                            }
                            else
                            {
                                boolean findSuccess = false;
                                if(c.getChildren() != null)
                                {
                                    for(Category child : c.getChildren())
                                    {
                                        if(Objects.equals(child.getName(), categoryName))
                                        {
                                            categoryNameList.add(c.getName());
                                            categoryNameList.addAll(c.getChildren().stream().map(category -> category.getName()).collect(Collectors.toList()));
                                            findSuccess = true;
                                            break;
                                        }
                                    }
                                }
                                if(findSuccess) break;
                            }
                        }

                    }
                    if( categoryNameList != null && categoryNameList.size() > 0)
                    {
                        binding.filterCategoryList.setSimpleItems(categoryNameList.toArray(new String[0]));
                    }
                }
            });

        }




        //region event more button
        binding.buttonMore.setOnClickListener(v ->{
            searchResultViewModel.getAllBooksByCategory(categoryName , language , sort , ++pageNumber);
        });
        //endregion

        //region Sort theo gia hoac ngay xuat ban
        binding.filterSortList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sortItem = adapterView.getItemAtPosition(i).toString();
                switch (sortItem)
                {
                    case "Giá tăng dần":
                        sort = "price,asc";
                        searchResultViewModel.getAllBooksByCategory(categoryName , language ,sort,pageNumber );
                        break;
                    case "Giá giảm dần" :
                        sort = "price,desc";
                        searchResultViewModel.getAllBooksByCategory(categoryName , language , sort , pageNumber);
                        break;
                    case "Mới nhất" :
                        sort = "publishcationDate,desc";
                        searchResultViewModel.getAllBooksByCategory(categoryName , language , sort , pageNumber);
                        break;
                    default:
                        return;
                }

            }
        });
        //endregion

        //region Filter theo ngon ngu
        binding.filterLanguageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String languageItem = adapterView.getItemAtPosition(i).toString();
                List<BookResponse> bookResponses = new ArrayList<>();
                if(languageItem.equals("Tất cả"))
                {
                    language = null;
                    searchResultViewModel.getAllBooksByCategory(categoryName , language , sort , pageNumber);
                }
                else
                {
                    language = languageItem;
                    searchResultViewModel.getAllBooksByCategory(categoryName , language , sort , pageNumber);
                }
            }
        });
        //endregion

        binding.filterCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pageNumber = oldPageNumber = 0;
                categoryName = adapterView.getItemAtPosition(i).toString();
                searchResultViewModel.getAllBooksByCategory(categoryName ,language,sort, pageNumber);
            }
        });
    }


}