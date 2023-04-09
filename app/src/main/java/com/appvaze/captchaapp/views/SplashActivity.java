package com.appvaze.captchaapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.appvaze.captchaapp.MainActivity;
import com.appvaze.captchaapp.R;
import com.appvaze.captchaapp.util.Constant;
import com.appvaze.captchaapp.views.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash );

        auth= FirebaseAuth.getInstance();
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        } ,2000);
    }

    private void checkUser() {
        FirebaseUser user=auth.getCurrentUser();
        if(user==null){
            startActivity( new Intent(SplashActivity.this, LoginActivity.class) );
        }else {
            startActivity( new Intent(SplashActivity.this, MainActivity.class) );

        }
        finish();
    }
}