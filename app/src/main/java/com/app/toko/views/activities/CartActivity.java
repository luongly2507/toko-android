package com.app.toko.views.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.adapters.CartItemAdapter;
import com.app.toko.databinding.ActivityCartBinding;
import com.app.toko.models.CartItem;
import com.app.toko.payload.request.UpdateCartItemRequest;
import com.app.toko.viewmodels.CartViewModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;


public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private CartItemAdapter cartItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        CartViewModel cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        binding.setCartViewModel(cartViewModel);

        setContentView(binding.getRoot());

        // Lấy userId và token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("toko-preferences", Context.MODE_PRIVATE);
        String userIdString = sharedPreferences.getString("user_id", null);
        String token = sharedPreferences.getString("access_token", null);

        boolean isConnectedToServer = false;
        if (userIdString != null && token != null) {
            cartViewModel.fetchCartItems(UUID.fromString(userIdString), token);
        }
        else {
            Toast.makeText(this, "Không nhận được token", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thiết lập sự kiện cho nút back
        binding.buttonBack.setOnClickListener(v -> {
            onBackPressed();
        });

        // Thiết lập sự kiện cho checkbox Tất cả
        binding.checkBoxSelectAll.setOnClickListener(view -> {
            boolean isChecked = binding.checkBoxSelectAll.isChecked();
            cartItemAdapter.checkAllItems(isChecked);
        });

        // Xóa các sản phẩm được check khỏi giỏ hàng
        binding.textViewDelete.setOnClickListener(view -> {
            List<CartItem> selectedItems = cartItemAdapter.getSelectedItems();
            for (CartItem item : selectedItems) {
                // Xóa phía người dùng
                cartItemAdapter.removeCartItem(cartItemAdapter.getCartItemList().indexOf(item));

                // Xóa phía database
                UUID bookId = UUID.fromString(item.getBookId());
                UUID userId = UUID.fromString(userIdString);
                cartViewModel.deleteCartItem(userId, bookId, token);
            }

            // Cập nhật lại giá tiền sau khi xóa
            BigDecimal totalPrice = cartItemAdapter.calculateTotalPrice();
            binding.textViewTotalPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi" , "VN")).format(totalPrice));

            // Bỏ check tất cả sau khi xóa
            binding.checkBoxSelectAll.setChecked(false);
        });

        // Khởi tạo RecyclerView và adapter
        RecyclerView recyclerView = binding.recyclerViewCart;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItemAdapter = new CartItemAdapter();
        recyclerView.setAdapter(cartItemAdapter);

        // Thêm sự kiện xóa các sản phẩm được chọn khỏi giỏ hàng
        cartItemAdapter.setOnItemClickListener(position -> {
            CartItem selectedItem = cartItemAdapter.getCartItemList().get(position);

            // Xóa phía người dùng, cập nhật lại tổng giá
            cartItemAdapter.removeCartItem(position);
            BigDecimal totalPrice = cartItemAdapter.calculateTotalPrice();
            binding.textViewTotalPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi" , "VN")).format(totalPrice));

            // Xóa ở database
            UUID userId = UUID.fromString(userIdString);
            UUID bookId = UUID.fromString(selectedItem.getBookId());
            cartViewModel.deleteCartItem(userId, bookId, token);
        });

        // Thêm sự kiện check/uncheck cho các item
        cartItemAdapter.setOnCartItemCheckedChangeListener((position, isChecked) -> {
            cartItemAdapter.getCartItemList().get(position).setChecked(isChecked);

            // Cập nhật lại tổng giá
            BigDecimal totalPrice = cartItemAdapter.calculateTotalPrice();
            binding.textViewTotalPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi" , "VN")).format(totalPrice));

            // Bỏ check tất cả nếu có 1 item không được check
            if (!isChecked) {
                binding.checkBoxSelectAll.setChecked(false);
            }
            else {
                // Kiểm tra xem tất cả các item có được check không
                boolean isAllChecked = cartItemAdapter
                        .getCartItemList()
                        .stream()
                        .allMatch(CartItem::isChecked);
                binding.checkBoxSelectAll.setChecked(isAllChecked);
            }
        });

        // Thêm sự kiện thay đổi số lượng sách
        cartItemAdapter.setOnCartItemChangeListener((position, newQuantity) -> {
            cartItemAdapter.notifyItemChanged(position);

            // Tính lại tổng tiền và cập nhật lên giao diện
            BigDecimal totalPrice = cartItemAdapter.calculateTotalPrice();
            binding.textViewTotalPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi", "VN")).format(totalPrice));

            // Lấy thông tin sách được thay đổi số lượng
            CartItem changedCartItem = cartItemAdapter.getCartItemList().get(position);
            UUID bookId = UUID.fromString(changedCartItem.getBookId());
            UUID userId = UUID.fromString(userIdString);

            // Cập nhật số lượng sách trong database
            UpdateCartItemRequest updateCartItemRequest = new UpdateCartItemRequest(bookId , newQuantity);
            cartViewModel.updateCartItem(userId, token, updateCartItemRequest);
        });


        // Lấy danh sách giỏ hàng từ server
        cartViewModel.getCartResponsesLiveData().observe(this, cartResponses -> {
            if (cartResponses != null) {
                List<CartItem> cartItems = cartResponses.stream()
                        .map(CartItem::fromCartResponse)
                        .collect(Collectors.toList());
                cartItemAdapter.setCartItemList(cartItems);

                // Hiển thị tổng giá tiền
                BigDecimal totalPrice = cartItemAdapter.calculateTotalPrice();
                binding.textViewTotalPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi" , "VN")).format(totalPrice));
            } else {
                Toast.makeText(this, "Không nhận được danh sách giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });

        binding.checkBoxSelectAll.setOnClickListener(view -> {
            boolean isChecked = binding.checkBoxSelectAll.isChecked();
            cartItemAdapter.checkAllItems(isChecked);
        });
    }
}
