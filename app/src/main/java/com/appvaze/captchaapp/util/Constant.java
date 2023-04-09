package com.appvaze.captchaapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.appvaze.captchaapp.settings.Settings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Constant {
    SharedPreferences sharedPreferences;
    Context context;

    public Constant(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        if (!sharedPreferences.contains("coin")) {
            sharedPreferences.edit().putInt("coin", 0).apply();
            sharedPreferences.edit().putBoolean("sub", false).apply();
        }
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString("email", email).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString("email", "");
    }

    public void setCoin(int coin) {
        sharedPreferences.edit().putInt("coin", coin).apply();
        if (Settings.SAVE_DATA_TO_FIREBASE) {
            setCoinToFirebase(coin);
        }
    }

    private void setCoinToFirebase(int coin) {
        Map<String, Object> map = new HashMap<>();
        map.put("coin", String.valueOf(coin));
        FirebaseFirestore.getInstance().collection("users_coins")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .set(map);
    }

    public int getCoin() {
        if(Settings.SAVE_DATA_TO_FIREBASE){
            getCoinToFirebase();
        }
        return sharedPreferences.getInt("coin", 0);
    }

    public void getCoinToFirebase() {
        String uuid = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("users_coins")
                .document(uuid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                try {
                                    String coin = document.getString("coin");
                                    if(coin!=null){
                                        sharedPreferences.edit().putInt("coin", Integer.parseInt(coin)).apply();
                                    }
                                }catch (Exception e ){

                                }

                            }

                        }
                    }
                });
    }

    public void setFreeCoin(boolean sub) {
        sharedPreferences.edit().putBoolean("sub", sub).apply();
    }

    public boolean getFreeCoin() {
        return sharedPreferences.getBoolean("sub", false);
    }

    public boolean getSub2() {
        return sharedPreferences.getBoolean("sub2", false);
    }

    public void setDailyLimit(int sub) {
        sharedPreferences.edit().putInt("daily_limit", sub).apply();
    }

    public int getDailyLimit() {
        return sharedPreferences.getInt("daily_limit", 0);
    }

    public void setDay(int sub) {
        sharedPreferences.edit().putInt("day", sub).apply();
    }

    public int getDay() {
        return sharedPreferences.getInt("day", -1);
    }

    public String getTodayDay() {
        DateFormat stringBuilder = new SimpleDateFormat("dd");
        Date date = new Date();
        return stringBuilder.format(date).toString();
    }
}
