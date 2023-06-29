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
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SearchResultBookActivity extends AppCompatActivity {
    private ActivitySearchResultBookBinding binding;
    private SearchResultBookViewModel searchResultBookViewModel;
    private BookRecyclerViewAdapter adapter;
    private List<BookResponse> bookList = new ArrayList<>();
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
            searchResultBookViewModel.getAllBooks();
            searchResultBookViewModel.getBookResponseLiveData().observe(this, new Observer<List<BookResponse>>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onChanged(List<BookResponse> bookResponses) {
                    if(bookResponses != null && bookResponses.size() > 0)
                    {
                        bookList = bookResponses.stream().filter(bookResponse -> bookResponse.getTitle().contains(searchName)).collect(Collectors.toList());
                        adapter = new BookRecyclerViewAdapter(bookList);
                        //Objects.requireNonNull(binding.reccyclerViewBookResult.getAdapter()).notifyDataSetChanged();
                        binding.reccyclerViewBookResult.setAdapter(adapter);
                    }
                }
            });
        }

        /*binding.buttonMore.setOnClickListener(v ->{
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
                        adapter = new BookRecyclerViewAdapter(bookList);
                        binding.reccyclerViewBookResult.setAdapter(adapter);
                    }
                });
            }
        });*/
        //endregion

        //region Sort theo gia hoac ngay xuat ban
        binding.filterSortList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sortItem = adapterView.getItemAtPosition(i).toString();
                switch (sortItem)
                {
                    case "Giá tăng dần":
                        bookList.sort(new Comparator<BookResponse>() {
                            @Override
                            public int compare(BookResponse bookResponse, BookResponse t1) {
                                return bookResponse.getPrice().compareTo(t1.getPrice());
                            }
                        });
                        adapter = new BookRecyclerViewAdapter(bookList);
                        binding.reccyclerViewBookResult.setAdapter(adapter);
                        break;
                    case "Giá giảm dần" :
                        bookList.sort(new Comparator<BookResponse>() {
                            @Override
                            public int compare(BookResponse bookResponse, BookResponse t1) {
                                return t1.getPrice().compareTo(bookResponse.getPrice());
                            }
                        });
                        adapter = new BookRecyclerViewAdapter(bookList);
                        binding.reccyclerViewBookResult.setAdapter(adapter);
                        break;
                    case "Mới nhất" :
                        bookList.sort(new Comparator<BookResponse>() {
                            @Override
                            public int compare(BookResponse bookResponse, BookResponse t1) {
                                try {
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date bookResponseDate = simpleDateFormat.parse(bookResponse.getPublishcationDate());
                                    Date t1Date = simpleDateFormat.parse(t1.getPublishcationDate());

                                    return bookResponseDate.compareTo(t1Date);
                                }catch(Exception ex)
                                {
                                    Log.e("FAIL!!" , "Fail to parse date from string");
                                }
                                return 0;
                            }
                        });
                        adapter = new BookRecyclerViewAdapter(bookList);
                        binding.reccyclerViewBookResult.setAdapter(adapter);
                        break;
                }
            }
        });
        //endregion

        //region Filter theo ngon ngu
        binding.filterLanguageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String laguageItem = adapterView.getItemAtPosition(i).toString();
                System.out.println(laguageItem + "Do chinh la item");
                List<BookResponse> bookResponses = new ArrayList<>();
                if(!laguageItem.equals("Tất cả"))
                {
                    bookResponses = bookList.stream().filter(bookResponse -> Objects.equals(bookResponse.getLanguage(), laguageItem)).collect(Collectors.toList());
                    adapter = new BookRecyclerViewAdapter(bookResponses);
                    binding.reccyclerViewBookResult.setAdapter(adapter);
                }
                else
                {
                    adapter = new BookRecyclerViewAdapter(bookList);
                    binding.reccyclerViewBookResult.setAdapter(adapter);
                }
            }
        });
        //endregion

    }
}