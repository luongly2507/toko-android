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
import android.view.View;
import android.widget.LinearLayout;

import com.app.toko.adapters.ContactRecyclerViewAdapter;
import com.app.toko.databinding.ActivityAddressSelectionBinding;
import com.app.toko.models.Contact;
import com.app.toko.viewmodels.AddressSelectionViewModel;
import com.app.toko.viewmodels.AddressViewModel;

import java.util.List;
import java.util.UUID;

public class AddressSelectionActivity extends AppCompatActivity {

    private ActivityAddressSelectionBinding mActivityAddressSelectionBinding;
    private View mView;
    private AddressSelectionViewModel addressSelectionViewModel;

    private SharedPreferences sharedPreferences;
    private String access_token;
    private String userIDStr;

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
        RecyclerView rcvContact = mActivityAddressSelectionBinding.recyclerViewAddress;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvContact.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvContact.addItemDecoration(itemDecoration);

        ContactRecyclerViewAdapter contactRecyclerViewAdapter = new ContactRecyclerViewAdapter(this, contacts);
        rcvContact.setAdapter(contactRecyclerViewAdapter);
    }
}