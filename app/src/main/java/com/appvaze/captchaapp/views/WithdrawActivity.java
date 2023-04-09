package com.appvaze.captchaapp.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appvaze.captchaapp.R;
import com.appvaze.captchaapp.settings.Settings;
import com.appvaze.captchaapp.util.Constant;
import com.appvaze.captchaapp.util.Loading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WithdrawActivity extends AppCompatActivity {
    private Constant constant;
    private TextView coin;
    private TextView withdrawLimit;
    private Button withdraw;
    private Loading loading;
    private FirebaseFirestore db;
    private RadioGroup radioGroup;
    private String paymentMethod = "Bank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        initUI();
    }

    private void initUI() {
        loading = new Loading(this);
        db = FirebaseFirestore.getInstance();
        coin = findViewById(R.id.coin);
        withdraw = findViewById(R.id.withdraw);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton button = findViewById(i);
                paymentMethod = button.getText().toString();
            }
        });
        withdrawLimit = findViewById(R.id.withdrawLimit);
        withdrawLimit.setText("You can withdraw on " + Settings.MINIMUM_COINS_TO_WITHDRAW);
        constant = new Constant(this);
        coin.setText("" + constant.getCoin());
        EditText editText = findViewById(R.id.payment_details);
        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (constant.getCoin() >= Settings.MINIMUM_COINS_TO_WITHDRAW) {
                    if (!editText.getText().toString().isEmpty()) {
                        addToDatabase("" + editText.getText().toString());
                    } else {
                        Toast.makeText(WithdrawActivity.this, "Please enter proper payment details", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(WithdrawActivity.this, "Minimum Points for withdraw is " + Settings.MINIMUM_COINS_TO_WITHDRAW, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addToDatabase(String s) {
        Constant constant = new Constant(this);
        loading.show();
        Map<String, Object> map = new HashMap<>();
        map.put("paymentDetails", s);
        map.put("paymentMethod", paymentMethod);
        db.collection("CaptchaUser").document(constant.getEmail())
                .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    constant.setCoin(0);
                                    loading.hide();
                                    Toast.makeText(WithdrawActivity.this, "Request Send Successfully", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            }, 3000);
                        } else {
                            loading.hide();
                            Toast.makeText(WithdrawActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}