package com.app.toko.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.R;
import com.app.toko.models.Book;
import com.app.toko.models.Order;
import com.app.toko.models.OrderDetail;
import com.app.toko.services.OrderDetailService;

import java.util.List;

public class OrderHistoryDetailRecyclerViewAdapter extends RecyclerView.Adapter<OrderHistoryDetailRecyclerViewAdapter.OrderHistoryDetailsViewHolder> {
    List<OrderDetail> orderDetails;
    Context mContext;

    public OrderHistoryDetailRecyclerViewAdapter(Context mContext, List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public OrderHistoryDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_history_detail_item , parent , false);
        mContext = parent.getContext();
        return new OrderHistoryDetailRecyclerViewAdapter.OrderHistoryDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryDetailsViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetails.get(position);
        holder.bookTitleTextView.setText(((Book) orderDetail.getBook()).getTitle());
        holder.amountTextView.setText(orderDetail.getQuantity().toString());
        holder.productValueTextView.setText(orderDetail.getPrice().toString());
    }

    @Override
    public int getItemCount() {
        if (orderDetails != null) {
            return orderDetails.size();
        }
        return 0;
    }

    public static class OrderHistoryDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitleTextView;
        TextView amountTextView;
        TextView productValueTextView;

        public OrderHistoryDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitleTextView = itemView.findViewById(R.id.bookTitleTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            productValueTextView = itemView.findViewById(R.id.productValueTextView);
        }
    }
}
