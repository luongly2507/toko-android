package com.app.toko.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.R;
import com.app.toko.models.Category;
import com.app.toko.views.activities.SearchBookActivity;

import java.util.ArrayList;

public class CategoryChildrenRecyclerViewAdapter extends RecyclerView.Adapter<CategoryChildrenRecyclerViewAdapter.ViewHolder>{
    private Context context;
    private ArrayList<String> listCategoryChildrenName;

    public CategoryChildrenRecyclerViewAdapter(Context context, ArrayList<String> listCategoryChildrenName) {
        this.context = context;
        this.listCategoryChildrenName =  listCategoryChildrenName;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.recycler_view_category_children_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = listCategoryChildrenName.get(position);
        holder.categoryChildrenName.setText(name);
        holder.categoryChildrenName.setOnClickListener(v -> {
            Intent intent = new Intent(context, SearchBookActivity.class);
            intent.putExtra("category",name);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (listCategoryChildrenName != null) {
            return listCategoryChildrenName.size();
        }
        return 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryChildrenName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryChildrenName = itemView.findViewById(R.id.textViewCategoryChildrenName);


        }
    }
}
