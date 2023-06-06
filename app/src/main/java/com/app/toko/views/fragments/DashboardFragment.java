package com.app.toko.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.adapters.CategoryRecyclerViewAdapter;
import com.app.toko.databinding.FragmentDashboardBinding;
import com.app.toko.models.Category;
import com.app.toko.viewmodels.DashboardViewModel;
import com.app.toko.views.activities.SearchBookActivity;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;

    private RecyclerView categoryRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        binding.buttonSearch.setOnClickListener(v -> {startActivity(new Intent(this.getContext(), SearchBookActivity.class));});
        View root = binding.getRoot();
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        dashboardViewModel.getAllCategories();
        dashboardViewModel.getCategoryResponsesLiveData().observe(getViewLifecycleOwner(), categoryResponses -> {
            categoryRecyclerView = binding.recyclerViewCategory;
            categoryRecyclerView.setAdapter(new CategoryRecyclerViewAdapter(this.getContext(), (ArrayList<Category>) categoryResponses));
            categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
