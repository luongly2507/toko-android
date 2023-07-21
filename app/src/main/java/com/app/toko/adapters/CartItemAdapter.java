package com.app.toko.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
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
    private OnCartItemCheckedChangeListener cartItemCheckedChangeListener;
    private OnCartItemQuantityChangedListener onCartItemQuantityChangedListener;
    private OnQuantityChangeByButtonListener quantityChangeListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnCartItemCheckedChangeListener {
        void onCartItemCheckedChange(int position, boolean isChecked);
    }

    public void setOnCartItemCheckedChangeListener(OnCartItemCheckedChangeListener listener) {
        this.cartItemCheckedChangeListener = listener;
    }

    public interface OnCartItemQuantityChangedListener {
        void onCartItemQuantityChanged(int position, int newQuantity);
    }

    public void setOnCartItemChangeListener(OnCartItemQuantityChangedListener listener) {
        this.onCartItemQuantityChangedListener = listener;
    }

    public interface OnQuantityChangeByButtonListener {
        void onQuantityIncrease(int position);

        void onQuantityDecrease(int position);
    }

    public void setOnQuantityChangeByButtonListener(OnQuantityChangeByButtonListener listener) {
        this.quantityChangeListener = listener;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Định nghĩa layout cho mục trong giỏ hàng (cart item)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_cart_item, parent, false);
        return new CartItemViewHolder(view, onItemClickListener, cartItemCheckedChangeListener, quantityChangeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        // Hiển thị dữ liệu của mục trong giỏ hàng tại vị trí position
        CartItem cartItem = cartItemList.get(position);
        holder.bindData(cartItem, onCartItemQuantityChangedListener);
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
            if (cartItem.isChecked()) {
                total = total.add(cartItem
                        .getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getCartQuantity())));
            }
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
        private EditText etQuantity;
        private CheckBox checkBox;
        private ImageButton imageButtonDelete;
        private ImageButton buttonPlus;
        private ImageButton buttonMinus;

        public CartItemViewHolder(@NonNull View itemView,
                                  OnItemClickListener onItemClickListener,
                                  OnCartItemCheckedChangeListener cartItemCheckedChangeListener,
                                  OnQuantityChangeByButtonListener quantityChangeListener) {
            super(itemView);
            ivBookImg = itemView.findViewById(R.id.imageViewBookImg);
            tvBookName = itemView.findViewById(R.id.textviewBookName);
            tvPrice = itemView.findViewById(R.id.textViewPrice);
            etQuantity = itemView.findViewById(R.id.editTextQuantity);
            checkBox = itemView.findViewById(R.id.checkBoxItem);
            imageButtonDelete = itemView.findViewById(R.id.imageButtonDelete); // Initialize the ImageButton

            imageButtonDelete.setOnClickListener(v -> {
                onItemClickListener.onItemClick(getAdapterPosition());
            });

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (cartItemCheckedChangeListener != null) {
                    cartItemCheckedChangeListener.onCartItemCheckedChange(getAdapterPosition(), isChecked);
                }
            });

            buttonPlus = itemView.findViewById(R.id.imageButtonPlus);
            buttonMinus = itemView.findViewById(R.id.imageButtonMinus);

            buttonPlus.setOnClickListener(v -> {
                quantityChangeListener.onQuantityIncrease(getAdapterPosition());
            });

            buttonMinus.setOnClickListener(v -> {
                quantityChangeListener.onQuantityDecrease(getAdapterPosition());
            });
        }

        public void bindData(CartItem cartItem, OnCartItemQuantityChangedListener onCartItemQuantityChangedListener) {
            // Hiển thị dữ liệu của mục trong giỏ hàng
            Glide.with(itemView)
                    .load(ApiService.SERVICE_BASE_URL + "img/upload/" + cartItem.getImgSource())
                    .into(ivBookImg);

            tvBookName.setText(cartItem.getTitle());
            etQuantity.setText(String.valueOf(cartItem.getCartQuantity()));

            String price = DecimalFormat.getCurrencyInstance(new Locale("vi" , "VN")).format(cartItem.getPrice());
            tvPrice.setText(price);

            checkBox.setChecked(cartItem.isChecked());

            etQuantity.setOnEditorActionListener((v, actionId, event) -> {
                if (!etQuantity.getText().toString().isEmpty()) {
                    // Xử lý khi người dùng nhập xong
                    String quantityText = etQuantity.getText().toString();
                    try {
                        int quantity = Integer.parseInt(quantityText);
                        // Kiểm tra số lượng nhập vào có hợp lệ không
                        if (quantity <= 0) {
                            etQuantity.setText("1");
                            quantity = 1;
                        }
                        else
                            if (quantity > cartItem.getStockQuantity()) {
                            etQuantity.setText(String.valueOf(cartItem.getStockQuantity()));
                            quantity = cartItem.getStockQuantity();
                            }

                        // Cập nhật cartItem với số lượng mới
                        cartItem.setCartQuantity(quantity);
                    } catch (NumberFormatException e) {
                        // Nếu nhập vào không phải số, tự động điền số lượng là 1
                        etQuantity.setText("1");
                        cartItem.setCartQuantity(1);
                    }

                    if (onCartItemQuantityChangedListener != null) {
                        int newQuantity = Integer.parseInt(etQuantity.getText().toString());
                        onCartItemQuantityChangedListener.onCartItemQuantityChanged(getAdapterPosition(), newQuantity);
                    }
                }

                etQuantity.clearFocus();
                return false;
            });

        }
    }
}
