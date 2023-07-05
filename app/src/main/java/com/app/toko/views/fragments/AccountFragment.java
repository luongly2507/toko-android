package com.app.toko.views.fragments;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app.toko.databinding.FragmentAccountBinding;
import com.app.toko.models.User;
import com.app.toko.viewmodels.AccountViewModel;
import com.app.toko.views.activities.LoginActivity;

import java.util.Objects;
import java.util.UUID;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private SharedPreferences sharedPreferences;
    private AccountViewModel accountViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        sharedPreferences = getContext().getSharedPreferences("toko-preferences", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("access_token", null) == null){
            startActivity(new Intent(this.getContext(), LoginActivity.class));
        } else {

            binding.setAccountViewModel(accountViewModel);
            binding.setLifecycleOwner(this);
            String access_token = sharedPreferences.getString("access_token" , null);
            String userIDStr = sharedPreferences.getString("user_id" , null);
            binding = FragmentAccountBinding.inflate(inflater, container, false);
            accountViewModel.getUserDetail(UUID.fromString(userIDStr) , access_token);
            accountViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onChanged(User user) {

                    if(user != null)
                    {
                        binding.textViewFullName.setText(user.getLastname() + user.getFirstname());
                        binding.textViewEmail.setText(user.getEmail());
                        binding.textViewPhone.setText(user.getPhone());
                    }

                }
            });
            binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("access_token" , null).apply();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            });


            return binding.getRoot();
        }
        return null;

    }
    public void checkAuthentication(){

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}