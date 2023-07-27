package com.app.toko.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.R;
import com.app.toko.models.CartItem;
import com.app.toko.models.OrderDetail;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class OrderHistoryDetailProductRecyclerViewAdapter extends RecyclerView.Adapter<OrderHistoryDetailProductRecyclerViewAdapter.OrderProductDetailViewHolder>{
    List<OrderDetail> orderDetails;

    public OrderHistoryDetailProductRecyclerViewAdapter(List<OrderDetail> orderDetails)
    {
        this.orderDetails = orderDetails;
    }

    @NonNull
    @Override
    public OrderHistoryDetailProductRecyclerViewAdapter.OrderProductDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_history_detail_item , parent , false);
        return new OrderProductDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryDetailProductRecyclerViewAdapter.OrderProductDetailViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetails.get(position);
        String price = DecimalFormat.getCurrencyInstance(new Locale("vi" , "VN")).format(orderDetail.getPrice());
        holder.price.setText(price);
        holder.amount.setText(String.valueOf(orderDetail.getQuantity()));
        holder.title.setText(orderDetail.getBook().getTitle());
    }

    @Override
    public int getItemCount() {
        if (orderDetails != null) return orderDetails.size();
        return 0;
    }

    public static class OrderProductDetailViewHolder extends RecyclerView.ViewHolder {
        TextView title , amount , price;
        public OrderProductDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bookTitleTextView);
            amount = itemView.findViewById(R.id.amountTextView);
            price = itemView.findViewById(R.id.productValueTextView);
        }
    }
}

