package com.app.toko.views.fragments;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
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
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private List<BookResponse> bookResponseList = new ArrayList<>();
    private BookRecyclerViewAdapter adapter;
    private ViewPagerAdapter bannerAdapter;
    private Boolean load = false;
    private List<Integer> bannerList = new ArrayList<>();
    private int pageNumber = 0 ;

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
        binding.tabLayoutTrending.addTab(binding.tabLayoutTrending.newTab().setText("Sách mới về"));
        binding.tabLayoutTrending.addTab(binding.tabLayoutTrending.newTab().setText("Sách ngoại ngữ mới"));
        binding.recyclerViewTrending.setLayoutManager(new GridLayoutManager(getActivity() , 2));
        bannerAdapter = new ViewPagerAdapter(bannerList);
        binding.viewPager2Banner.setAdapter(bannerAdapter);
        binding.tabLayoutTrending.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String sort;
                switch (tab.getPosition())
                {
                    case 0:
                        sort = "quantity,desc";
                        homeViewModel.getAllBooksByPurchase(sort);
                        break;
                    case 1:
                        sort = "publishcationDate,desc";
                        homeViewModel.getAllBooksBySort(sort , "");
                        break;
                    case 2:
                        sort = "publishcationDate,desc";
                        homeViewModel.getAllBooksBySort(sort , "Tiếng Anh");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        homeViewModel.getAllBooksByPurchase("quantity,desc");
        homeViewModel.getBookResponseLivaData().observe(getViewLifecycleOwner(), new Observer<List<BookResponse>>() {
            @Override
            public void onChanged(List<BookResponse> bookResponses) {
                if(bookResponses != null)
                {
                    if(bookResponses.size() < 10 && bookResponseList.size() > 0)
                    {
                        Random random = new Random();
                        for(int i = 0 ; i < 10 ;)
                        {
                            int number = random.nextInt(bookResponseList.size());
                            if(!bookResponses.contains(bookResponseList.get(number)))
                            {
                                bookResponses.add(bookResponseList.get(number));
                                i++;
                            }
                        }
                    }
                    adapter = new BookRecyclerViewAdapter(bookResponses.stream().limit(10).collect(Collectors.toList()));
                    binding.recyclerViewTrending.setAdapter(adapter);
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}