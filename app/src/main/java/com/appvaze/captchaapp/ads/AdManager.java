package com.appvaze.captchaapp.ads;

import android.app.Activity;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.appvaze.captchaapp.MainActivity;

public class AdManager {
    private static MaxInterstitialAd interstitialAd;
    private static boolean isAdLoaded = false;

    public static void loadInterstitialAd(Activity activity, String s) {
        if (!isAdLoaded) {
            interstitialAd = new MaxInterstitialAd("fb7b4e60bb1181a6", activity);
            interstitialAd.setListener(new MaxAdListener() {
                @Override
                public void onAdLoaded(MaxAd ad) {
                    // Reklam yüklendiğinde yapılacak işlemler
                    isAdLoaded = true;
                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {
                    // Reklam yüklenemediğinde yapılacak işlemler
                    isAdLoaded = false;
                }

                @Override
                public void onAdDisplayed(MaxAd ad) {
                    // Reklam gösterildiğinde yapılacak işlemler
                    isAdLoaded = false;
                }

                @Override
                public void onAdHidden(MaxAd ad) {
                    // Reklam gizlendiğinde yapılacak işlemler
                    isAdLoaded = false;
                }

                @Override
                public void onAdClicked(MaxAd ad) {
                    // Reklama tıklandığında yapılacak işlemler
                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                    // Reklam gösterilemediğinde yapılacak işlemler
                    isAdLoaded = false;
                }
            });
            interstitialAd.loadAd();
        }
    }

    public static void showInterstitialAd(MainActivity mainActivity) {
        if (isAdLoaded) {
            interstitialAd.showAd();
        }
    }
}
