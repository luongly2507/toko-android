package com.app.toko.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.R;
import com.app.toko.models.CartItem;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class OrderProductRecyclerViewAdapter extends RecyclerView.Adapter<OrderProductRecyclerViewAdapter.OrderProductViewHolder> {
    List<CartItem> cartItemList;

    public OrderProductRecyclerViewAdapter(List<CartItem> cartItemList)
    {
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public OrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_history_product_item , parent , false);
        return new OrderProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        String price = DecimalFormat.getCurrencyInstance(new Locale("vi" , "VN")).format(cartItem.getPrice());
        holder.price.setText(price);
        holder.amount.setText(String.valueOf(cartItem.getCartQuantity()));
        holder.title.setText(cartItem.getTitle());
    }

    @Override
    public int getItemCount() {
        if (cartItemList != null) return cartItemList.size();
        return 0;
    }

    public static class OrderProductViewHolder extends RecyclerView.ViewHolder {
        TextView title , amount , price;
        public OrderProductViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bookTitleTextView);
            amount = itemView.findViewById(R.id.amountTextView);
            price = itemView.findViewById(R.id.productValueTextView);
        }
    }
}
