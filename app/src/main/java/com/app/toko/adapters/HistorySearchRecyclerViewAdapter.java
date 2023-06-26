package com.app.toko.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HistorySearchRecyclerViewAdapter extends RecyclerView.Adapter<HistorySearchRecyclerViewAdapter.HistoryViewHolder> {

    private List<String> searchList = new ArrayList<>();
    private Context mContext;
    private EditText editTextSearch;

    public List<String> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<String> searchList) {
        this.searchList = searchList;
    }

    public HistorySearchRecyclerViewAdapter(List<String> searchList)
    {
        this.searchList = searchList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_history_search_item , parent , false);
        mContext = parent.getContext();
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        String searchName = searchList.get(position);
        holder.historySearch.setText(searchList.get(position));
        holder.historyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                SharedPreferences preferences = mContext.getSharedPreferences("HISTORY_SEARCH" , Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putStringSet("HistorySet" , new HashSet<>(searchList)).apply();
            }
        });
        holder.cardViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextSearch != null)
                {
                    editTextSearch.setText(searchName);
                }
            }
        });
    }

    public void setEditTextSearch(EditText editTextSearch) {
        this.editTextSearch = editTextSearch;
    }

    @Override
    public int getItemCount() {
        if(searchList != null) return searchList.size();
        return 0;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView historyDelete;
        TextView historySearch;
        MaterialCardView cardViewHistory;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            historyDelete = itemView.findViewById(R.id.image_view_history_delete);
            historySearch = itemView.findViewById(R.id.text_view_history_search);
            cardViewHistory = itemView.findViewById(R.id.card_view_history);
        }
    }


}
