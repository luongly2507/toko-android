package com.app.toko.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.toko.R;
import com.app.toko.adapters.BookRecyclerViewAdapter;
import com.app.toko.adapters.ViewPagerAdapter;
import com.app.toko.databinding.FragmentHomeBinding;
import com.app.toko.payload.response.BookResponse;
import com.app.toko.viewmodels.HomeViewModel;
import com.app.toko.views.activities.CartActivity;
import com.app.toko.views.activities.SearchBookActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private List<BookResponse> bookResponseList = new ArrayList<>();
    private BookRecyclerViewAdapter adapter;
    private ViewPagerAdapter bannerAdapter;
    private List<Integer> bannerList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        bannerList.add(R.drawable.banner1);
        bannerList.add(R.drawable.banner2);
        bannerList.add(R.drawable.banner3);
        bannerList.add(R.drawable.banner4);
        bannerList.add(R.drawable.banner5);
        binding.buttonSearch.setOnClickListener(v -> {startActivity(new Intent(this.getContext(), SearchBookActivity.class));});
        binding.buttonCart.setOnClickListener(v -> {startActivity(new Intent(this.getContext(), CartActivity.class));});
        binding.tabLayoutTrending.addTab(binding.tabLayoutTrending.newTab().setText("Xu hướng mỗi ngày"));
        binding.tabLayoutTrending.addTab(binding.tabLayoutTrending.newTab().setText("Sách HOT - Giảm sốc"));
        binding.tabLayoutTrending.addTab(binding.tabLayoutTrending.newTab().setText("Bestseller ngoại ngữ"));
        binding.recyclerViewTrending.setLayoutManager(new GridLayoutManager(getActivity() , 2));
        binding.viewPager2Banner.setAdapter(new ViewPagerAdapter(bannerList));
        homeViewModel.getAllBooks();
        homeViewModel.getBookResponseLivaData().observe(getViewLifecycleOwner(), new Observer<List<BookResponse>>() {
            @Override
            public void onChanged(List<BookResponse> bookResponses) {
                bookResponseList = bookResponses;
                adapter = new BookRecyclerViewAdapter(bookResponseList);
                binding.recyclerViewTrending.setAdapter(adapter);
            }
        });

        /*binding.tabLayoutTrending.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}