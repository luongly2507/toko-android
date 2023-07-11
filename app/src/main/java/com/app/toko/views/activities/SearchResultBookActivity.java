package com.app.toko.views.activities;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.app.toko.R;
import com.app.toko.adapters.BookRecyclerViewAdapter;
import com.app.toko.databinding.ActivitySearchResultBookBinding;
import com.app.toko.payload.response.BookResponse;
import com.app.toko.viewmodels.SearchResultBookViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SearchResultBookActivity extends AppCompatActivity {
    private ActivitySearchResultBookBinding binding;
    private SearchResultBookViewModel searchResultBookViewModel;
    private BookRecyclerViewAdapter adapter;
    private List<BookResponse> bookList = new ArrayList<>();
    private String language = null , sort = null;
    private int pageNumber = 0 , oldPageNumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchResultBookBinding.inflate(getLayoutInflater());
        searchResultBookViewModel = new ViewModelProvider(this).get(SearchResultBookViewModel.class);
        setContentView(binding.getRoot());
        binding.reccyclerViewBookResult.setLayoutManager(new GridLayoutManager(this , 2));
        binding.buttonSearch.setOnClickListener(v -> {startActivity(new Intent(this, SearchBookActivity.class));});
        binding.buttonCart.setOnClickListener(v -> {startActivity(new Intent(this, CartActivity.class));});
        binding.buttonBack.setOnClickListener(v -> {
            onBackPressed();
        });
        String searchName = getIntent().getStringExtra("SearchName");
        if(searchName != null && !searchName.isBlank())
        {
            binding.buttonSearch.setText(searchName);
            searchResultBookViewModel.getAllBookByTitle(searchName , language , sort , pageNumber);
            searchResultBookViewModel.totalPages.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if(integer - 1 <= pageNumber)
                    {
                        //binding.buttonMore.setVisibility(GONE);
                        binding.textViewNothing.setVisibility(View.VISIBLE);
                    }
                    else binding.buttonMore.setVisibility(View.VISIBLE);
                }
            });
            searchResultBookViewModel.getBookResponseLiveData().observe(this, new Observer<List<BookResponse>>() {
                @Override
                public void onChanged(List<BookResponse> bookResponses) {
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
        }

        //region Event button more
        binding.buttonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchResultBookViewModel.getAllBookByTitle(searchName , language , sort , ++pageNumber);
            }
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
                        searchResultBookViewModel.getAllBookByTitle(searchName ,language, sort, pageNumber);
                        break;
                    case "Giá giảm dần" :
                        sort = "price,desc";
                        searchResultBookViewModel.getAllBookByTitle(searchName ,language, sort , pageNumber);
                        break;
                    case "Mới nhất" :
                        sort = "publishcationDate,desc";
                        searchResultBookViewModel.getAllBookByTitle(searchName ,language,sort ,pageNumber);
                        break;
                }
            }
        });
        //endregion

        //region Filter theo ngon ngu
        binding.filterLanguageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String languageItem = adapterView.getItemAtPosition(i).toString();
                System.out.println(languageItem + "Do chinh la item");
                List<BookResponse> bookResponses = new ArrayList<>();
                if(languageItem.equals("Tất cả"))
                {
                    language = null;
                    searchResultBookViewModel.getAllBookByTitle(searchName ,language, sort ,pageNumber);
                }
                else
                {
                    language = languageItem;
                    searchResultBookViewModel.getAllBookByTitle(searchName , language , sort , pageNumber);
                }
            }
        });
        //endregion

    }
}