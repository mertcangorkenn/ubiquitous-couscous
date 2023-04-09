package com.appvaze.captchaapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.appvaze.captchaapp.ads.AdManager;
import com.appvaze.captchaapp.settings.Settings;
import com.appvaze.captchaapp.util.Constant;
import com.appvaze.captchaapp.util.Loading;
import com.appvaze.captchaapp.views.WithdrawActivity;
import com.appvaze.captchaapp.views.auth.LoginActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private TextView coin, captcha, status;
    private EditText text;
    private Button submit;
    private Constant constant;
    private String _captcha;
    private ImageView skip;
    private int _counter = 0;
    private int _adCounter = 0;
    private int _dailyCounterLimit = 0;
    private Loading loading;
    private static final String TAG = "MainActivityTAG";

    private Button showAdButton;

    private AdManager adManager;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
        navBar();
        checkInternet();

        showAdButton = findViewById(R.id.submit);
        adManager = new AdManager(this);

        showAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adManager.loadInterstitialAd();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkInternet() {
        if (!isNetworkConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Please Check your internet connection and try again");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (isNetworkConnected()) {
                        dialogInterface.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "Please Try Again !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.show();
        }
    }

    private void initUi() {
        constant = new Constant(this);
        skip = findViewById(R.id.skip);
        captcha = findViewById(R.id.captcha);
        coin = findViewById(R.id.coin);
        text = findViewById(R.id.payment_details);
        submit = findViewById(R.id.submit);
        status = findViewById(R.id.status);
        if (constant.getDay() != Integer.parseInt(constant.getTodayDay()) || constant.getDailyLimit() <= Settings.DAILY_CAPTCHA_LIMIT) {
            _dailyCounterLimit = constant.getDailyLimit();
            submit.setEnabled(true);
        } else {
            submit.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Today Limit is Completed", Toast.LENGTH_SHORT).show();
        }
        loading = new Loading(this);
        _captcha = getSaltString().toLowerCase();
        captcha.setText(_captcha);
        coin.setText(String.valueOf(constant.getCoin()));
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                checkInternet();
                String _text = text.getText().toString();
                if (!_text.isEmpty()) {
                    if (_captcha.equals(_text)) {
                        status.setText(_captcha + " is correct!");
                        _captcha = getSaltString().toLowerCase();
                        captcha.setText(_captcha);
                        text.setText("");
                        _counter++;
                        _adCounter++;
                        if (_counter > Settings.COINS_FOR_RATING && !constant.getFreeCoin()) {
                            showRatingDialog();
                        }
                        constant.setCoin(constant.getCoin() + Settings.COINS_TO_BE_ADDED);
                        coin.setText(String.valueOf(constant.getCoin()));
                        _dailyCounterLimit++;
                        checkStatus(_dailyCounterLimit);
                    } else {
                        status.setText(_captcha + "  doesn't match with " + _text);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Enter Captcha !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _captcha = getSaltString().toLowerCase();
                captcha.setText(_captcha);
            }
        });
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < Settings.LENGTH_OF_CAPTCHA) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private void navBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        drawer = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.withdraw:
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, WithdrawActivity.class));
                break;
            case R.id.share:
                drawer.closeDrawer(GravityCompat.START);
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String shareMessage = "\nCheck out this Great Application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Select any"));
                } catch (Exception e) {
                    Log.d(TAG, "onNavigationItemSelected: " + e.toString());
                }
                break;
            case R.id.rate:
                drawer.closeDrawer(GravityCompat.START);
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finishAffinity();
                break;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void checkStatus(int dailyCounterLimit) {
        if (dailyCounterLimit >= Settings.DAILY_CAPTCHA_LIMIT) {
            setTodayDate();
            submit.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Today Limit is Completed", Toast.LENGTH_SHORT).show();
        } else {
            constant.setDailyLimit(dailyCounterLimit);
        }
    }

    private void setTodayDate() {
        constant.setDay(Integer.parseInt(constant.getTodayDay()));
    }


    private void showRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Get 300 Coins NOW !!");
        builder.setMessage("Please rate us 5 stars to get 300 coins for free, don't miss this chance");
        builder.setPositiveButton("Rate 5 star 🌟", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showRateBottomSheet();
            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showRateBottomSheet() {
        ReviewManager manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(tasks -> {
                    if (constant.getFreeCoin()) {
                        final String appPackageName = getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    } else {
                        constant.setFreeCoin(true);
                        constant.setCoin(constant.getCoin() + Settings.COINS_AFTER_RATING);
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(constant!=null){
            coin.setText(String.valueOf(constant.getCoin()));
        }
    }
}

