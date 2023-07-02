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
        binding.buttonSearch.setOnClickListener(v -> {startActivity(new Intent(this, SearchBookActivity.class));});
        binding.buttonCart.setOnClickListener(v -> {startActivity(new Intent(this, CartActivity.class));});
        binding.reccyclerViewBookResult.setLayoutManager(new GridLayoutManager(this , 2));
        String categoryName = getIntent().getStringExtra("category");

        if (categoryName != null){
            binding.filterCategoryList.setText(categoryName);
            searchResultViewModel.getAllBooksByCategory(categoryName , pageNumber);
            searchResultViewModel.getBookResponseLiveData().observe(this, new Observer<List<BookResponse>>() {
                @Override
                public void onChanged(List<BookResponse> bookResponses) {
                    bookList.addAll(bookResponses);
                    adapter = new BookRecyclerViewAdapter(bookList);
                    binding.reccyclerViewBookResult.setAdapter(adapter);
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
                                categoryNameList.addAll(c.getChildren().stream().map(category -> category.getName()).collect(Collectors.toList()));
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
            if(!searchResultViewModel.getMoreBook(categoryName , ++pageNumber))
            {
                binding.buttonMore.setVisibility(GONE);
                binding.textViewNothing.setVisibility(View.VISIBLE);
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
                String laguageItem = adapterView.getItemAtPosition(i).toString();
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

        binding.filterCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String categoryName = adapterView.getItemAtPosition(i).toString();
                searchResultViewModel.getAllBooksByCategory(categoryName , 0);
                BookResponseLiveDataObserve();

            }
        });
    }

    public void BookResponseLiveDataObserve()
    {
        searchResultViewModel.getBookResponseLiveData().observe(this, new Observer<List<BookResponse>>() {
                    @Override
                    public void onChanged(List<BookResponse> bookResponses) {
                        adapter = new BookRecyclerViewAdapter(bookResponses);
                        binding.reccyclerViewBookResult.setAdapter(adapter);
                    }
                });
    }

}