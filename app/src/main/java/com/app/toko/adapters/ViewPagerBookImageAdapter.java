package com.app.toko.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.R;
import com.app.toko.utils.ApiService;
import com.bumptech.glide.Glide;

import java.util.List;

public class ViewPagerBookImageAdapter extends RecyclerView.Adapter<ViewPagerBookImageAdapter.BookImageViewHolder> {
    private List<String> imageList;
    private Context mContext;

    public ViewPagerBookImageAdapter(List<String> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public BookImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_pager_2_banner_item , parent , false);
        return new BookImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookImageViewHolder holder, int position) {
        String imageSrc = imageList.get(position);
        if(imageSrc != null && !imageSrc.isBlank())
        {
            Glide.with(mContext)
                    .load(ApiService.SERVICE_BASE_URL + "img/upload/" + imageSrc)
                    .optionalCenterCrop()
                    .into(holder.bookImage);
        }
    }

    @Override
    public int getItemCount() {
        if(imageList != null) return imageList.size();
        return 0;
    }

    public static class BookImageViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        public BookImageViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.image_view_banner);
        }
    }
}
