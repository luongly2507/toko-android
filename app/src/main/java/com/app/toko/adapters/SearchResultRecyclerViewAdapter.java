package com.app.toko.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.R;
import com.app.toko.payload.response.AlbumResponse;
import com.app.toko.payload.response.BookResponse;
import com.app.toko.repositories.BookRespository;
import com.app.toko.utils.ApiService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class SearchResultRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultRecyclerViewAdapter.SearchResultViewHolder> {

    private List<BookResponse> bookResponseList;
    private Context context;

    public SearchResultRecyclerViewAdapter(List<BookResponse> bookResponseList) {
        this.bookResponseList = bookResponseList;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_book_item , parent , false);
        return new SearchResultViewHolder(view);
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        BookResponse bookResponse = bookResponseList.get(position);
        if(bookResponse != null)
        {
            holder.title.setText(bookResponse.getTitle());
            holder.price.setText(bookResponse.getPrice().toString());
            String imageSource = "";
            for (AlbumResponse i: bookResponse.getAlbums()) {
                imageSource = i.getImageSource();
                if(i.isPresentation())
                {
                    imageSource = i.getImageSource();
                    break;
                }
            }
            Glide.with(context)
                    .load(ApiService.SERVICE_BASE_URL + "img/upload/" + imageSource)
                    .into(holder.avatar);

        }
    }

    @Override
    public int getItemCount() {
        if(bookResponseList != null) return bookResponseList.size();
        return 0;
    }

    public static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView price , title;
        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.image_view_book_avatar);
            price = itemView.findViewById(R.id.text_view_price);
            title = itemView.findViewById(R.id.text_view_title);
        }
    }
}
