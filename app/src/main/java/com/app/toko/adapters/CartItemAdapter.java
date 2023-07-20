package com.app.toko.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.R;
import com.app.toko.models.CartItem;
import com.app.toko.utils.ApiService;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<CartItem> cartItemList;

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Định nghĩa layout cho mục trong giỏ hàng (cart item)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_cart_item, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        // Hiển thị dữ liệu của mục trong giỏ hàng tại vị trí position
        CartItem cartItem = cartItemList.get(position);
        holder.bindData(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItemList != null ? cartItemList.size() : 0;
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivBookImg;
        private TextView tvBookName;
        private TextView tvPrice;
        private TextView tvQuantity;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBookImg = itemView.findViewById(R.id.imageViewBookImg);
            tvBookName = itemView.findViewById(R.id.textviewBookName);
            tvPrice = itemView.findViewById(R.id.textViewPrice);
            tvQuantity = itemView.findViewById(R.id.editTextQuantity);
        }

        public void bindData(CartItem cartItem) {
            // Hiển thị dữ liệu của mục trong giỏ hàng
            Glide.with(itemView)
                    .load(ApiService.SERVICE_BASE_URL + "img/upload/" + cartItem.getImgSource())
                    .into(ivBookImg);

            tvBookName.setText(cartItem.getTitle());
            tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

            String price = DecimalFormat.getCurrencyInstance(new Locale("vi" , "VN")).format(cartItem.getPrice());
            tvPrice.setText(price);
        }
    }
}
