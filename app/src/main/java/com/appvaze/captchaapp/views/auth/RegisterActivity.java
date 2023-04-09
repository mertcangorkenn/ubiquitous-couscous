package com.appvaze.captchaapp.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appvaze.captchaapp.MainActivity;
import com.appvaze.captchaapp.R;
import com.appvaze.captchaapp.util.Constant;
import com.appvaze.captchaapp.util.Loading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText email, pass, name;
    private Button register;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUi();
    }

    private void registerUser(String email, String pass, String username) {
        loading.show();
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Constant constant = new Constant(RegisterActivity.this);
                    constant.setEmail(email);
                    loading.hide();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finishAffinity();
                } else {
                    loading.hide();
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void initUi() {
        loading = new Loading(this);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        name = findViewById(R.id.name);
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _email = email.getText().toString().trim();
                String _pass = pass.getText().toString().trim();
                String _username = name.getText().toString().trim();
                if (!_email.isEmpty() && !_pass.isEmpty() && !_username.isEmpty()) {
                    registerUser(_email, _pass, _username);
                } else {
                    Toast.makeText(RegisterActivity.this, "Enter all details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}