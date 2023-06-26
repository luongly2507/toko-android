package com.app.toko.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.toko.R;
import com.app.toko.adapters.HistorySearchRecyclerViewAdapter;
import com.app.toko.databinding.ActivitySearchBookBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SearchBookActivity extends AppCompatActivity {

    private ActivitySearchBookBinding binding;
    private Set<String> historySet = new HashSet<>();
    private List<String> historyList = new ArrayList<>();
    private HistorySearchRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.arrowBackIcon.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.editTextSearchBook.setOnClickListener(v -> {startActivity(new Intent(this, SearchBookActivity.class));});

        binding.recyclerViewSearchHistory.setLayoutManager(new FlexboxLayoutManager(this , FlexDirection.ROW));
        adapter = new HistorySearchRecyclerViewAdapter(historyList);
        adapter.setEditTextSearch(binding.editTextSearchBook);
        binding.recyclerViewSearchHistory.setAdapter(adapter);
        getValueSharedPreferences();
        binding.editTextSearchBook.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if(textView.getText() != null)
                    {
                        historySet.add(textView.getText().toString());
                        setValueSharedPreferences(historySet);
                        startIntentToSearchResult(textView.getText().toString());
                        handled = true;
                    }
                }
                return handled;
            }
        });

    }
    public void startIntentToSearchResult(String searchName)
    {
        Intent intent = new Intent(this , SearchResultBookActivity.class);
        intent.putExtra("SearchName" , searchName);
        startActivity(intent);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setValueSharedPreferences(Set<String> historySet)
    {
        SharedPreferences preferences = this.getSharedPreferences("HISTORY_SEARCH" , MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("HistorySet" , historySet).apply();
        adapter.setSearchList(new ArrayList<>(historySet));
        Objects.requireNonNull(binding.recyclerViewSearchHistory.getAdapter()).notifyDataSetChanged();
        /*HistorySearchRecyclerViewAdapter adapter = new HistorySearchRecyclerViewAdapter(history);
        adapter.setEditTextSearch(binding.editTextSearchBook);
        binding.recyclerViewSearchHistory.setAdapter(adapter);*/

    }
    @SuppressLint("NotifyDataSetChanged")
    public void getValueSharedPreferences()
    {
        SharedPreferences preferences = this.getSharedPreferences("HISTORY_SEARCH" , MODE_PRIVATE);
        Set<String> historySet1 = preferences.getStringSet("HistorySet" , null);
        if(historySet1 != null)
        {
            List<String> history = new ArrayList<>();
            history.addAll(historySet1);
            historySet = historySet1;
            adapter.setSearchList(new ArrayList<>(historySet1));
            Objects.requireNonNull(binding.recyclerViewSearchHistory.getAdapter()).notifyDataSetChanged();
            /*HistorySearchRecyclerViewAdapter adapter = new HistorySearchRecyclerViewAdapter(history);
            adapter.setEditTextSearch(binding.editTextSearchBook);
            binding.recyclerViewSearchHistory.setAdapter(adapter);*/
        }
    }
}