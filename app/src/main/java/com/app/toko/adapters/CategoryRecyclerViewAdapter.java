package com.app.toko.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.R;
import com.app.toko.models.Category;
import com.app.toko.views.activities.SearchBookActivity;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Category> categories;

    public CategoryRecyclerViewAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.recycler_view_category_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.categoryParentName.setText(category.getName());
        holder.categoryParentName.setOnClickListener(v -> {
            Intent intent = new Intent(context, SearchBookActivity.class);
            intent.putExtra("category",category.getName());
            context.startActivity(intent);
        });
        ArrayList<String> listCategoryChildrenName = new ArrayList<>();
        if (category.getChildren() != null ){
            listCategoryChildrenName = (ArrayList<String>) category.getChildren()
                                                .stream()
                                                .map(c -> c.getName())
                                                .collect(Collectors.toList());
        }
        holder.categoryChildrenRecyclerView.setAdapter(new CategoryChildrenRecyclerViewAdapter(context,listCategoryChildrenName));
        holder.categoryChildrenRecyclerView.setLayoutManager(new GridLayoutManager(context,2));

    }



    @Override
    public int getItemCount() {
        if (categories != null) {
            return categories.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryParentName;

        private RecyclerView categoryChildrenRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryParentName = itemView.findViewById(R.id.textViewCategoryParentName);
            categoryChildrenRecyclerView = itemView.findViewById(R.id.recyclerViewCategoryChildren);


        }
    }
}
