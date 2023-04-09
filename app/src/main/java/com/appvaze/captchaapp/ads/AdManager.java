package com.appvaze.captchaapp.ads;

import android.app.Activity;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;

public class AdManager {
    private MaxInterstitialAd interstitialAd;
    private Activity activity;

    public AdManager(Activity activity) {
        this.activity = activity;
    }

    public void loadInterstitialAd() {
        interstitialAd = new MaxInterstitialAd("YOUR_AD_UNIT_ID", activity);
        interstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                // Reklam yüklendiğinde yapılacak işlemler
                interstitialAd.showAd();
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                // Reklam yüklenemediğinde yapılacak işlemler
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                // Reklam gösterildiğinde yapılacak işlemler
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                // Reklam gizlendiğinde yapılacak işlemler
            }

            @Override
            public void onAdClicked(MaxAd ad) {
                // Reklama tıklandığında yapılacak işlemler
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                // Reklam gösterilemediğinde yapılacak işlemler
            }
        });

        interstitialAd.loadAd();
    }
}
