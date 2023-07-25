package com.app.toko.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.toko.databinding.RecyclerViewAddressItemBinding;
import com.app.toko.models.Contact;

import java.util.List;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> {

    private List<Contact> mListContact;
    private Context mContext;
    private int checkPosition = 0;
    public ContactRecyclerViewAdapter(Context context, List<Contact> mListContact) {
        this.mListContact = mListContact;
        this.mContext = context;
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


        if (contact.getReceiver() == null)
        {
            holder.recyclerViewAddressItemBinding.itemHT.setText("I am testuser");
        }
        else holder.recyclerViewAddressItemBinding.itemHT.setText(contact.getReceiver());

        holder.recyclerViewAddressItemBinding.itemSDT.setText(contact.getTelephone());
        holder.recyclerViewAddressItemBinding.itemDC.setText(contact.getLine() + ", " + contact.getWard() + ", " + contact.getDistrict() + ", " + contact.getCity());

        if (checkPosition == position) {
            holder.recyclerViewAddressItemBinding.addressChecked.setVisibility(View.VISIBLE);
            holder.recyclerViewAddressItemBinding.defaultFlag.setVisibility(View.VISIBLE);
            holder.recyclerViewAddressItemBinding.defaultText.setVisibility(View.VISIBLE);
        } else {
            holder.recyclerViewAddressItemBinding.addressChecked.setVisibility(View.GONE);
            holder.recyclerViewAddressItemBinding.defaultFlag.setVisibility(View.GONE);
            holder.recyclerViewAddressItemBinding.defaultText.setVisibility(View.GONE);
        }

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

    public int getSelected(){
        if (checkPosition != -1){
            return checkPosition;
        }
        return -1;
    }

}
