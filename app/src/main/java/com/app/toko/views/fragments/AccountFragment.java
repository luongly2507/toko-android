package com.app.toko.views.fragments;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.toko.databinding.FragmentAccountBinding;
import com.app.toko.viewmodels.AccountViewModel;
import com.app.toko.views.activities.LoginActivity;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private SharedPreferences sharedPreferences;
    private AccountViewModel accountViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        sharedPreferences = getContext().getSharedPreferences("toko-preferences", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("access_token", null) == null){
            startActivity(new Intent(this.getContext(), LoginActivity.class));
        } else {
            accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
            binding = FragmentAccountBinding.inflate(inflater, container, false);
            View root = binding.getRoot();

            binding.setLoginViewModel(accountViewModel);
            binding.setLifecycleOwner(this);
            return root;
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