package com.app.toko.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.R;
import com.app.toko.models.CartItem;
import com.app.toko.utils.ApiService;
import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<CartItem> cartItemList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Định nghĩa layout cho mục trong giỏ hàng (cart item)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_cart_item, parent, false);
        return new CartItemViewHolder(view, onItemClickListener);
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

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
        notifyDataSetChanged();
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public BigDecimal calculateTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cartItemList) {
            total = total.add(cartItem.getTotalPrice());
        }
        return total;
    }

    public void checkAllItems(boolean isChecked) {
        for (CartItem cartItem : cartItemList) {
            cartItem.setChecked(isChecked);
        }
        notifyDataSetChanged();
    }

    public List<CartItem> getSelectedItems() {
        List<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : cartItemList) {
            if (item.isChecked()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    public void removeCartItem(int position) {
        if (position >= 0 && position < cartItemList.size()) {
            cartItemList.remove(position);
            notifyItemRemoved(position);
        }
    }


    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivBookImg;
        private TextView tvBookName;
        private TextView tvPrice;
        private TextView tvQuantity;
        private CheckBox checkBox;
        private ImageButton imageButtonDelete;
        private CartItemAdapter cartItemAdapter;

        public CartItemViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ivBookImg = itemView.findViewById(R.id.imageViewBookImg);
            tvBookName = itemView.findViewById(R.id.textviewBookName);
            tvPrice = itemView.findViewById(R.id.textViewPrice);
            tvQuantity = itemView.findViewById(R.id.editTextQuantity);
            checkBox = itemView.findViewById(R.id.checkBoxItem);
            imageButtonDelete = itemView.findViewById(R.id.imageButtonDelete); // Initialize the ImageButton

            imageButtonDelete.setOnClickListener(v -> {
                onItemClickListener.onItemClick(getAdapterPosition());
            });


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

            checkBox.setChecked(cartItem.isChecked());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                cartItem.setChecked(isChecked);
            });
        }
    }
}
