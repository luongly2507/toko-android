package com.app.toko.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.R;
import com.app.toko.payload.response.CartResponse;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<CartResponse> cartResponseList;

    public void setCartItemList(List<CartResponse> cartResponseList) {
        this.cartResponseList = cartResponseList;
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
        CartResponse cartResponse = cartResponseList.get(position);
        holder.bindData(cartResponse);
    }

    @Override
    public int getItemCount() {
        return cartResponseList != null ? cartResponseList.size() : 0;
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tvBookName;
        private TextView tvQuantity;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.textviewBookName);
            tvQuantity = itemView.findViewById(R.id.editTextQuantity);
        }

        public void bindData(CartResponse cartResponse) {
            // Hiển thị dữ liệu của mục trong giỏ hàng
//            tvBookName.setText(cartItem.getBookId());
//            tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
        }
    }
}
