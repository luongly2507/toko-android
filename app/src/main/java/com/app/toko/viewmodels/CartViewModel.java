package com.app.toko.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.models.CartItem;
import com.app.toko.models.Contact;
import com.app.toko.payload.request.UpdateCartItemRequest;
import com.app.toko.payload.response.BookResponse;
import com.app.toko.payload.response.CartResponse;
import com.app.toko.repositories.ContactRepository;
import com.app.toko.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private ContactRepository contactRepository;
    public MutableLiveData<List<Contact>> contactListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<CartResponse>> cartResponsesLiveData;
    private MutableLiveData<List<CartItem>> selectedItemsLiveData = new MutableLiveData<>();
    public MutableLiveData<String> address = new MutableLiveData<>();

    public CartViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        cartResponsesLiveData = userRepository.getCartResponsesLiveData();
        contactRepository = new ContactRepository(getApplication());
        contactListLiveData = contactRepository.getListMutableLiveData();
    }

    public MutableLiveData<List<CartResponse>> getCartResponsesLiveData() {
        return cartResponsesLiveData;
    }

    public void fetchCartItems(UUID userId, String token) {
        userRepository.getUserCartItems(userId, token);
    }

    public void deleteCartItem(UUID userId, UUID bookId, String token) {
        userRepository.deleteCartItem(userId, bookId, token);
    }

    public void updateCartItem(UUID userId, String token, UpdateCartItemRequest updateCartItemRequest) {
        userRepository.updateCartItem(userId, token, updateCartItemRequest);
    }

    public BookResponse getBookResponseForCartItem(CartItem cartItem) {
        List<CartResponse> cartResponses = cartResponsesLiveData.getValue();
        if (cartResponses != null) {
            for (CartResponse cartResponse : cartResponses) {
                UUID bookId = UUID.fromString(cartItem.getBookId());
                if (cartResponse.getBookResponse().getId().equals(bookId)) {
                    return cartResponse.getBookResponse();
                }
            }
        }
        return null;
    }

    public void fetchContacts(UUID userId, String token) {
        contactRepository.LoadContact(userId, "Bearer " + token);
    }

    public Contact getDefaultContact(List<Contact> contacts) {
        for (Contact contact : contacts) {
            if (contact.getDefault()) {
                return contact;
            }
        }
        return null;
    }

    public void setTextAddress(List<Contact> contacts) {
        if(contacts != null && contacts.size() > 0) {
            Contact defaultContact = getDefaultContact(contacts);
            if (defaultContact != null) {
                address.setValue(defaultContact.getReceiver() + " | " + defaultContact.getTelephone() + "\n" +
                        defaultContact.getLine()
                        + ", " + defaultContact.getWard()
                        + ", " + defaultContact.getDistrict()
                        + ", " + defaultContact.getCity());
            }
            else {
                address.setValue("Không tìm thấy địa chỉ giao hàng");
            }
        }
        else {
            address.setValue("Không tìm thấy địa chỉ giao hàng");
        }

    }

    public MutableLiveData
            <List<Contact>> getContactListLiveData() {
        return contactListLiveData;
    }
}
