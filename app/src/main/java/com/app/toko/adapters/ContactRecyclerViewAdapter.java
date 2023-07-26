package com.app.toko.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.R;
import com.app.toko.databinding.RecyclerViewAddressItemBinding;
import com.app.toko.models.Contact;
import com.app.toko.viewmodels.AddressSelectionViewModel;
import com.app.toko.views.activities.AddressActivity;
import com.app.toko.views.activities.AddressSelectionActivity;

import org.checkerframework.checker.units.qual.C;

import java.security.acl.Owner;
import java.util.List;
import java.util.UUID;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> {
    private AddressSelectionViewModel mAddressSelectionViewModel;
    private String access_token;
    private String userIDStr;
    private List<Contact> mListContact;
    private Context mContext;
    private Owner mOwner;

    private int checkPosition = -1;
    private int setDefault = -1;
    private boolean start = true;


    public ContactRecyclerViewAdapter(Context context, List<Contact> mListContact, AddressSelectionViewModel addressSelectionViewModel, String access_token, String userIDStr) {
        this.mListContact = mListContact;
        this.mContext = context;
        this.mAddressSelectionViewModel = addressSelectionViewModel;
        this.access_token = access_token;
        this.userIDStr = userIDStr;
        checkNoneDefult();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerViewAddressItemBinding recyclerViewAddressItemBinding = RecyclerViewAddressItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ContactViewHolder(recyclerViewAddressItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {


        Contact contact = mListContact.get(position);
        if (contact == null)
        {
            Log.d("Contact", "is null");
            return;
        }


        if (contact.getReceiver() != null)
        {
            holder.recyclerViewAddressItemBinding.itemHT.setText(contact.getReceiver());
        }

        holder.recyclerViewAddressItemBinding.itemSDT.setText(contact.getTelephone());
        holder.recyclerViewAddressItemBinding.itemDC.setText(contact.getLine() + ", " + contact.getWard() + ", " + contact.getDistrict() + ", " + contact.getCity());

        if (contact.getDefault() && start == true)
        {
            checkPosition = holder.getAdapterPosition();
            setDefault = holder.getAdapterPosition();
            start = false;
        }

        if (checkPosition == position) {
            holder.recyclerViewAddressItemBinding.addressChecked.setVisibility(View.VISIBLE);

        } else {
            holder.recyclerViewAddressItemBinding.addressChecked.setVisibility(View.GONE);

        }

        if (setDefault == position){
            holder.recyclerViewAddressItemBinding.defaultFlag.setVisibility(View.VISIBLE);
            holder.recyclerViewAddressItemBinding.defaultText.setVisibility(View.VISIBLE);
        }
        else {
            holder.recyclerViewAddressItemBinding.defaultFlag.setVisibility(View.GONE);
            holder.recyclerViewAddressItemBinding.defaultText.setVisibility(View.GONE);
        }

        // Popup Menu
        holder.recyclerViewAddressItemBinding.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, holder.getAdapterPosition(), contact.getId());
            }
        });


        // Check Position
        holder.recyclerViewAddressItemBinding.layoutItemAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousPosition = checkPosition;
                checkPosition = holder.getAdapterPosition();
                notifyItemChanged(previousPosition);
                notifyItemChanged(checkPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListContact != null){
            return mListContact.size();
        }
        return 0;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{
        private RecyclerViewAddressItemBinding recyclerViewAddressItemBinding;
        public ContactViewHolder(@NonNull RecyclerViewAddressItemBinding recyclerViewAddressItemBinding) {
            super(recyclerViewAddressItemBinding.getRoot());
            this.recyclerViewAddressItemBinding = recyclerViewAddressItemBinding;
        }


    }

    private void showPopupMenu(View view, int position, String id){
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        popupMenu.inflate(R.menu.address_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.address_default:

                        int previousPosition = checkPosition;
                        checkPosition = position;

                        notifyItemChanged(previousPosition);
                        notifyItemChanged(checkPosition);

                        int previousDefault = setDefault;
                        setDefault = position;


                        Contact preContact = mListContact.get(previousDefault);
                        Contact updateContact = mListContact.get(position);

                        UUID preID = UUID.fromString(preContact.getId());
                        UUID updateID = UUID.fromString(updateContact.getId());
                        preContact.setDefault(false);
                        updateContact.setDefault(true);

                        mAddressSelectionViewModel.getContactRepository().UpdateContact(new MutableLiveData<>(preContact),UUID.fromString(userIDStr),"Bearer " + access_token,preID);
                        mAddressSelectionViewModel.getContactRepository().UpdateContact(new MutableLiveData<>(updateContact),UUID.fromString(userIDStr),"Bearer " + access_token,updateID);

                        notifyItemChanged(previousDefault);
                        notifyItemChanged(setDefault);

                        break;

                    case R.id.address_update:
                        Intent intent = new Intent(mContext, AddressActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("update Contact", mListContact.get(position));
                        bundle.putBoolean("is Update", true);
                        intent.putExtra("Data Address", bundle);
                        mContext.startActivity(intent);
                        break;

                    case R.id.address_delete:
                        mAddressSelectionViewModel.DeleteContact(UUID.fromString(userIDStr),"Bearer " + access_token, UUID.fromString(id));
                        int oldSize = mListContact.size();
                        mListContact = mAddressSelectionViewModel.mListMutableLiveData.getValue();
                        if(mListContact.size() != oldSize)
                        {
                            if (position == checkPosition){
                                checkPosition = 0;
                                notifyItemChanged(checkPosition);
                            }
                            if (position == setDefault) {
                                setDefault = 0;
                                notifyItemChanged(setDefault);
                            }
                        }
                        notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    public Contact getSelected(){
        if (checkPosition == -1){
            return null;
        }
        return mListContact.get(checkPosition);
    }

    // Xét TH không có item nào có default = true
    public void checkNoneDefult()
    {
        int defaultPosition = -1;
        for (int i = 0; i < mListContact.size(); i++) {
            Contact contact = mListContact.get(i);
            if (contact != null && contact.getDefault()) {
                defaultPosition = i;
                break;
            }
        }

        // Nếu không có mục nào được đặt làm mặt định, đặt mục đầu tiên (vị trí 0) làm mặt định
        if (defaultPosition == -1 && mListContact.size() > 0) {
            setDefault = 0;
            mListContact.get(setDefault).setDefault(true);
            UUID updateID = UUID.fromString(mListContact.get(setDefault).getId());
            mAddressSelectionViewModel.getContactRepository().UpdateContact(new MutableLiveData<>(mListContact.get(setDefault)), UUID.fromString(userIDStr), "Bearer " + access_token, updateID);
            notifyDataSetChanged();
        }
    }


    public void updateContactList() {
        mAddressSelectionViewModel.initData(UUID.fromString(userIDStr), "Bearer " + access_token);
        notifyDataSetChanged();
    }

    public void release()
    {
        mContext = null;
    }

}