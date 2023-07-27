package com.app.toko.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.R;
import com.app.toko.models.Order;
import com.app.toko.models.OrderDetail;
import com.app.toko.views.activities.OrderHistoryActivity;
import com.app.toko.views.activities.OrderHistoryDetailActivity;

import java.util.List;


public class OrderHistoryRecyclerViewAdapter extends RecyclerView.Adapter<OrderHistoryRecyclerViewAdapter.OrderHistoryViewHolder> {
    List<Order> orders;
    Context mContext;

    public OrderHistoryRecyclerViewAdapter(List<Order> orders, Context mContext) {
        this.orders = orders;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_history_order_item , parent , false);
        mContext = parent.getContext();
        return new OrderHistoryRecyclerViewAdapter.OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        Order order = orders.get(position);
        if (order == null) {
            Log.d("Contact", "is null");
            return;
        }
        holder.orderIdTextView.setText(order.getId().toString());
        holder.orderDateTextView.setText(order.getPurchaseDate().toString());
        List<OrderDetail> orderDetails= order.getOrderDetails();
        holder.orderHistoryDetailsRecyclerView.setAdapter(
                new OrderHistoryDetailRecyclerViewAdapter(mContext, orderDetails)
        );
        holder.orderHistoryDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        holder.seeDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OrderHistoryDetailActivity.class);
                intent.putExtra("order_data", order);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (orders != null) {return orders.size();}
        return 0;
    }

    public static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView;
        TextView orderDateTextView;
        RecyclerView orderHistoryDetailsRecyclerView;
        Button seeDetailsButton;
        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            orderHistoryDetailsRecyclerView = itemView.findViewById(R.id.orderHistoryDetailsRecyclerView);
            seeDetailsButton = itemView.findViewById(R.id.seeDetailsButton);
        }
    }
}
