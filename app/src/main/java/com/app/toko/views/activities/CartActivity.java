package com.app.toko.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.adapters.CartItemAdapter;
import com.app.toko.databinding.ActivityCartBinding;
import com.app.toko.models.CartItem;
import com.app.toko.models.Contact;
import com.app.toko.payload.request.UpdateCartItemRequest;
import com.app.toko.payload.response.BookResponse;
import com.app.toko.viewmodels.CartViewModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;


public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private CartItemAdapter cartItemAdapter;
    private ActivityResultLauncher<Intent> getAddressLauncher;
    private String address = "";

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

        if (userIdString != null && token != null) {
            cartViewModel.fetchCartItems(UUID.fromString(userIdString), token);
            cartViewModel.fetchContacts(UUID.fromString(userIdString), token);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        builder.setTitle("Xác nhận xóa sách");
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        // Xóa các sản phẩm được check khỏi giỏ hàng
        binding.textViewDelete.setOnClickListener(view -> {
            builder.setMessage("Bạn có chắc chắn muốn xóa các sản phẩm đã chọn?");
            builder.setPositiveButton("Xóa", (dialogInterface, i) -> {
                List<CartItem> selectedItems = cartItemAdapter.getSelectedItems();
                for (CartItem item : selectedItems) {
                    // Xóa phía người dùng

                    boolean isEmpty = cartItemAdapter.removeCartItem(cartItemAdapter
                            .getCartItemList()
                            .indexOf(item));
                    if (isEmpty) {
                        binding.recyclerViewCart.setVisibility(View.GONE);
                        binding.textViewEmptyCartMessage.setVisibility(View.VISIBLE);
                    }

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
            builder.show();
        });

        // Khởi tạo RecyclerView và adapter
        RecyclerView recyclerView = binding.recyclerViewCart;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItemAdapter = new CartItemAdapter();
        recyclerView.setAdapter(cartItemAdapter);

        cartItemAdapter.setOnItemClickListener(new CartItemAdapter.OnItemClickListener() {
            // Thêm sự kiện xóa các sản phẩm bằng nút icon delete
            @Override
            public void onItemDeleteClick(int position) {
                builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
                builder.setPositiveButton("Xóa", (dialogInterface, i) -> {
                    CartItem selectedItem = cartItemAdapter.getCartItemList().get(position);

                    // Xóa phía người dùng, cập nhật lại tổng giá
                    boolean isEmpty = cartItemAdapter.removeCartItem(position);
                    if (isEmpty) {
                        binding.recyclerViewCart.setVisibility(View.GONE);
                        binding.textViewEmptyCartMessage.setVisibility(View.VISIBLE);
                    }

                    BigDecimal totalPrice = cartItemAdapter.calculateTotalPrice();
                    binding.textViewTotalPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi" , "VN")).format(totalPrice));

                    // Xóa ở database
                    UUID userId = UUID.fromString(userIdString);
                    UUID bookId = UUID.fromString(selectedItem.getBookId());
                    cartViewModel.deleteCartItem(userId, bookId, token);
                });
                builder.show();
            }

            // Thêm sự kiện chuyển sang màn hình chi tiết sách khi click vào item
            @Override
            public void onItemClick(CartItem cartItem, Context context) {
                BookResponse bookResponse = cartViewModel.getBookResponseForCartItem(cartItem);

                if (bookResponse != null) {
                    Intent intent = new Intent(context , BookDetailActivity.class);
                    intent.putExtra("BookDetail" , bookResponse);
                    context.startActivity(intent);
                }

            }
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

        // Thêm sự kiện thay đổi số lượng sách bằng EditText
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

        // Thêm sự kiện thay đổi số lượng sách bằng nút +/-
        cartItemAdapter.setOnQuantityChangeByButtonListener(new CartItemAdapter.OnQuantityChangeByButtonListener() {
            @Override
            public void onQuantityIncrease(int position) {
                CartItem changedCartItem = cartItemAdapter.getCartItemList().get(position);
                if(changedCartItem.getCartQuantity() < changedCartItem.getStockQuantity()) {
                    changedCartItem.setCartQuantity(changedCartItem.getCartQuantity() + 1);
                    cartItemAdapter.notifyItemChanged(position);

                    // Tính lại tổng tiền và cập nhật lên giao diện
                    BigDecimal totalPrice = cartItemAdapter.calculateTotalPrice();
                    binding.textViewTotalPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi", "VN")).format(totalPrice));

                    // Lấy thông tin sách được thay đổi số lượng
                    UUID bookId = UUID.fromString(changedCartItem.getBookId());
                    UUID userId = UUID.fromString(userIdString);

                    // Cập nhật số lượng sách trong database
                    UpdateCartItemRequest updateCartItemRequest = new UpdateCartItemRequest(bookId , changedCartItem.getCartQuantity());
                    cartViewModel.updateCartItem(userId, token, updateCartItemRequest);
                }
                else {
                    Toast.makeText(CartActivity.this, "Số lượng sách trong giỏ hàng đã đạt giới hạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onQuantityDecrease(int position) {
                CartItem changedCartItem = cartItemAdapter.getCartItemList().get(position);

                // Giảm số lượng sách trong giỏ hàng
                if(changedCartItem.getCartQuantity() > 1) {
                    changedCartItem.setCartQuantity(changedCartItem.getCartQuantity() - 1);
                    cartItemAdapter.notifyItemChanged(position);

                    // Tính lại tổng tiền và cập nhật lên giao diện
                    BigDecimal totalPrice = cartItemAdapter.calculateTotalPrice();
                    binding.textViewTotalPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi", "VN")).format(totalPrice));

                    // Lấy thông tin sách được thay đổi số lượng
                    UUID bookId = UUID.fromString(changedCartItem.getBookId());
                    UUID userId = UUID.fromString(userIdString);

                    // Cập nhật số lượng sách trong database
                    UpdateCartItemRequest updateCartItemRequest = new UpdateCartItemRequest(bookId , changedCartItem.getCartQuantity());
                    cartViewModel.updateCartItem(userId, token, updateCartItemRequest);
                }
                else {
                    // Xóa phía người dùng, cập nhật lại tổng giá
                    builder.setMessage("Bạn có chắc chắn muốn xóa sách này khỏi giỏ hàng?");
                    builder.setPositiveButton("Xóa", (dialog, which) -> {
                        boolean isEmpty = cartItemAdapter.removeCartItem(position);
                        if (isEmpty) {
                            binding.recyclerViewCart.setVisibility(View.GONE);
                            binding.textViewEmptyCartMessage.setVisibility(View.VISIBLE);
                        }

                        BigDecimal totalPrice = cartItemAdapter.calculateTotalPrice();
                        binding.textViewTotalPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi" , "VN")).format(totalPrice));

                        // Xóa ở database
                        UUID userId = UUID.fromString(userIdString);
                        UUID bookId = UUID.fromString(changedCartItem.getBookId());
                        cartViewModel.deleteCartItem(userId, bookId, token);
                    });
                    builder.show();
                }
            }
        });


        // Lấy danh sách giỏ hàng từ server
        cartViewModel.getCartResponsesLiveData().observe(this, cartResponses -> {
            if (cartResponses != null && cartResponses.size() > 0) {
                binding.recyclerViewCart.setVisibility(View.VISIBLE);
                binding.textViewEmptyCartMessage.setVisibility(View.GONE);

                List<CartItem> cartItems = cartResponses.stream()
                        .map(CartItem::fromCartResponse)
                        .collect(Collectors.toList());
                cartItemAdapter.setCartItemList(cartItems);

                // Hiển thị tổng giá tiền
                BigDecimal totalPrice = cartItemAdapter.calculateTotalPrice();
                binding.textViewTotalPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi" , "VN")).format(totalPrice));
            } else {
                binding.recyclerViewCart.setVisibility(View.GONE);
                binding.textViewEmptyCartMessage.setVisibility(View.VISIBLE);
            }
        });

        binding.checkBoxSelectAll.setOnClickListener(view -> {
            boolean isChecked = binding.checkBoxSelectAll.isChecked();
            cartItemAdapter.checkAllItems(isChecked);
        });

        getAddressLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Xử lý kết quả thành công
                        Intent data = result.getData();
                        if (data != null) {
                            address = data.getStringExtra("address");
                            cartViewModel.address.setValue(address);
                        }

                    }
                });

        binding.cardViewUserInfo.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddressSelectionActivity.class);
            intent.putExtra("from" , "CartActivity");
            getAddressLauncher.launch(intent);
        });

        binding.buttonBuy.setOnClickListener(v -> {
            if (cartItemAdapter.getCartItemList().size() > 0) {
                if (cartItemAdapter.getSelectedItems().isEmpty())
                    Toast.makeText(CartActivity.this, "Vui lòng chọn sách", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        Contact contact = cartViewModel.getDefaultContact(cartViewModel.contactListLiveData.getValue());
                        Intent intent = new Intent(CartActivity.this, ConfirmOrderActivity.class);
                        intent.putExtra("selectedItems", (Serializable) cartItemAdapter.getSelectedItems());
                        intent.putExtra("contact_id" , contact.getId().toString());
                        startActivity(intent);
                    }
                    catch (Exception ex)
                    {
                        System.out.println(ex.toString());
                    }
                }
            }
            else {
                Toast.makeText(CartActivity.this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            }
        });

        cartViewModel.getContactListLiveData().observe(this, contacts -> {
            cartViewModel.contactListLiveData.postValue(contacts);
            cartViewModel.setTextAddress(contacts);
        });
    }
}
