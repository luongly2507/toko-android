package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.view.View;
import android.widget.CompoundButton;

import com.app.toko.R;
import com.app.toko.databinding.ActivityUpdateInfoBinding;
import com.app.toko.models.User;
import com.app.toko.payload.request.UpdateUserInfoRequest;
import com.app.toko.viewmodels.UpdateInfoViewModel;

import java.util.Base64;
import java.util.UUID;

public class UpdateInfoActivity extends AppCompatActivity {
    ActivityUpdateInfoBinding binding;
    SharedPreferences sharedPreferences;
    UpdateInfoViewModel updateInfoViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateInfoBinding.inflate(getLayoutInflater());
        updateInfoViewModel = new ViewModelProvider(this).get(UpdateInfoViewModel.class);
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this);
        binding.setUpdateInfoViewModel(updateInfoViewModel);
        sharedPreferences = getSharedPreferences("toko-preferences" , MODE_PRIVATE);
        String access_token = sharedPreferences.getString("access_token" , null);
        String userIDStr = sharedPreferences.getString("user_id" , null);
        binding.checkBoxChangePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    binding.linearLayoutChangePassword.setVisibility(View.VISIBLE);
                }
                else binding.linearLayoutChangePassword.setVisibility(View.GONE);
            }
        });
        updateInfoViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null)
                {
                    updateInfoViewModel.firstname.postValue(user.getFirstname());
                    updateInfoViewModel.lastname.postValue(user.getLastname());
                    updateInfoViewModel.email.postValue(user.getEmail());
                    updateInfoViewModel.phone.postValue(user.getPhone());
                }
            }
        });
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if(access_token != null)
        {
            updateInfoViewModel.getUserDetail(UUID.fromString(userIDStr) , access_token);
            binding.buttonAcceptChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean isCheck  = binding.checkBoxChangePassword.isChecked();
                    if(updateInfoViewModel.isValidUser(isCheck))
                    {
                        if(isCheck)
                        {
                            updateInfoViewModel.updateUserPassword(updateInfoViewModel.phone.getValue() , updateInfoViewModel.newPass.getValue());
                        }
                        UpdateUserInfoRequest updateUserInfoRequest = new UpdateUserInfoRequest(updateInfoViewModel.firstname.getValue(),
                                updateInfoViewModel.lastname.getValue() ,
                                updateInfoViewModel.email.getValue());
                        updateInfoViewModel.updateUserInfo(UUID.fromString(userIDStr) , updateUserInfoRequest , access_token);
                        Intent intent = new Intent(UpdateInfoActivity.this , MainActivity.class);
                        intent.putExtra("toFrag" , "account");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }

    }
}