package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.app.toko.R;
import com.app.toko.adapters.ContactRecyclerViewAdapter;
import com.app.toko.databinding.ActivityAddressSelectionBinding;
import com.app.toko.models.Contact;
import com.app.toko.viewmodels.AddressSelectionViewModel;
import com.app.toko.viewmodels.AddressViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddressSelectionActivity extends AppCompatActivity {

    private ActivityAddressSelectionBinding mActivityAddressSelectionBinding;
    private View mView;
    private AddressSelectionViewModel addressSelectionViewModel;

    private SharedPreferences sharedPreferences;
    private String access_token;
    private String userIDStr;

    private RecyclerView rcvContact;
    private ContactRecyclerViewAdapter contactRecyclerViewAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityAddressSelectionBinding = ActivityAddressSelectionBinding.inflate(getLayoutInflater());
        mView = mActivityAddressSelectionBinding.getRoot();
        addressSelectionViewModel = new ViewModelProvider(this).get(AddressSelectionViewModel.class);


        mActivityAddressSelectionBinding.setLifecycleOwner(this);
        mActivityAddressSelectionBinding.setAddressSelectionViewModel(addressSelectionViewModel);
        setContentView(mView);

        sharedPreferences = this.getSharedPreferences("toko-preferences", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("access_token", null) == null){
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            access_token = sharedPreferences.getString("access_token" , null);
            userIDStr = sharedPreferences.getString("user_id" , null);
        }

        // RecyclerView
            loadContact();


        // Button Back
        mActivityAddressSelectionBinding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Button Register
        mActivityAddressSelectionBinding.btnCreateNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressSelectionActivity.this, AddressActivity.class);
                startActivity(intent);
            }
        });

        // Select Address (Button OK)
        mActivityAddressSelectionBinding.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact contact = contactRecyclerViewAdapter.getSelected();
                if (contact != null) {
                    Intent intent = new Intent(AddressSelectionActivity.this, CartActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Cart Contact", contact);
                    intent.putExtra("Data Address for Cart", bundle);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(AddressSelectionActivity.this, "Bundle null", Toast.LENGTH_SHORT).show();
                }
                finish();
            }

        });

    }
    public void loadContact(){
        addressSelectionViewModel.initData(UUID.fromString(userIDStr),"Bearer " + access_token);
        addressSelectionViewModel.getContactRepository().getListMutableLiveData().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                addressSelectionViewModel.mListMutableLiveData.postValue(contacts);
                displayListContacts(contacts);

            }
        });
    }


    private void displayListContacts(List<Contact> contacts) {
        rcvContact = mActivityAddressSelectionBinding.recyclerViewAddress;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvContact.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvContact.addItemDecoration(itemDecoration);

        contactRecyclerViewAdapter = new ContactRecyclerViewAdapter(this, contacts, addressSelectionViewModel,access_token, userIDStr);
        rcvContact.setAdapter(contactRecyclerViewAdapter);
        contactRecyclerViewAdapter.notifyDataSetChanged();
    }


    // Cập nhật dữ liệu và hiển thị RecyclerView tại đây khi AddressActivity kết thúc và AddressSelectionActivity trở lại foreground
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStart() {
        super.onStart();
        addressSelectionViewModel.initData(UUID.fromString(userIDStr),"Bearer " + access_token);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (contactRecyclerViewAdapter != null)
        {
            contactRecyclerViewAdapter.release();
        }
    }

}