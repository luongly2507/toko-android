package com.app.toko.views.activities;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.app.toko.adapters.SearchResultRecyclerViewAdapter;
import com.app.toko.databinding.ActivitySearchResultBinding;
import com.app.toko.payload.response.BookResponse;
import com.app.toko.viewmodels.SearchResultViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private ActivitySearchResultBinding binding;
    private SearchResultViewModel searchResultViewModel;
    private SearchResultRecyclerViewAdapter adapter;
    private List<BookResponse> bookList = new ArrayList<>();

    private int pageNumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        searchResultViewModel = new ViewModelProvider(this).get(SearchResultViewModel.class);
        binding.buttonBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.reccyclerViewBookResult.setLayoutManager(new GridLayoutManager(this , 2));
        String categoryName = getIntent().getStringExtra("category");
        if (categoryName != null){
            searchResultViewModel.getAllBooksByCategory(categoryName , pageNumber);
            searchResultViewModel.getBookResponseLiveData().observe(this, new Observer<List<BookResponse>>() {
                @Override
                public void onChanged(List<BookResponse> bookResponses) {
                    bookList.addAll(bookResponses);
                    adapter = new SearchResultRecyclerViewAdapter(bookList);
                    binding.reccyclerViewBookResult.setAdapter(adapter);
                }
            });
        }
        binding.buttonMore.setOnClickListener(v ->{
            if(!searchResultViewModel.getMoreBook(categoryName , ++pageNumber))
            {
                binding.buttonMore.setVisibility(GONE);
            }
            else
            {
                searchResultViewModel.getBookResponseLiveData().observe(this, new Observer<List<BookResponse>>() {
                    @Override
                    public void onChanged(List<BookResponse> bookResponses) {
                        bookList.addAll(bookResponses);
                        adapter = new SearchResultRecyclerViewAdapter(bookList);
                        binding.reccyclerViewBookResult.setAdapter(adapter);
                    }
                });
            }
        });

    }

}