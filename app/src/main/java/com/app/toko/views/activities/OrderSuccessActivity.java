package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.app.toko.R;
import com.app.toko.databinding.ActivityOrderSuccessBinding;

public class OrderSuccessActivity extends AppCompatActivity {

    private static final int DELAY_TIME = 2000; // Thời gian chờ 2 giây

    private TextView countdownTextView;
    private int countdownValue = 2; // Giá trị đếm ngược ban đầu

    private Handler handler;
    private Runnable runnable;
    private ActivityOrderSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String money = getIntent().getStringExtra("   money");
        binding.moneyPrepareTextView.setText(money);
        countdownTextView = findViewById(R.id.countdownTextView);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                countdownValue--;
                String countdownText = " " + countdownValue + " giây";
                countdownTextView.setText(countdownText);

                if (countdownValue == 0) {
                    // Chuyển đến màn hình đăng nhập sau khi đếm ngược hoàn thành
                    startLoginActivity();
                } else {
                    // Tiếp tục đếm ngược
                    handler.postDelayed(this, 1000); // Đặt thời gian chờ 1 giây
                }
            }
        };

        // Bắt đầu đếm ngược
        handler.postDelayed(runnable, 1000); // Đặt thời gian chờ 1 giây
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Đóng Activity hiện tại
    }
}